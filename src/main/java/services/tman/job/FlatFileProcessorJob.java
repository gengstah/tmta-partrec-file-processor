package services.tman.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import services.tman.control.TMANFileProcessor;

/**
 * This class is of type <code>QuartzJobBean</code> 
 * which overrides the executeInternal() method.
 * This method implements the scheduling functionality
 * 
 * @author Gerard
 * @version 1.0.0
 * @since March 2013
 */
public class FlatFileProcessorJob extends QuartzJobBean {
	private static final Logger logger = Logger.getLogger(FlatFileProcessorJob.class);
	private List<TMANFileProcessor> processors;
	
	public FlatFileProcessorJob() { }

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		for(TMANFileProcessor fileProcessor : processors) {
			for(String directoryName : fileProcessor.getInputDirectories()) {
				File input = new File(directoryName);
				String []flatFileNames = input.list();
				
				for(String flatFileName : flatFileNames) {
					File flatFile = new File(directoryName + "\\" + flatFileName);
					if(flatFile.isDirectory()) continue;
					if(isFileNotYetTransferredCompletely(flatFile)) continue;
					
					processTMTAFlatFile(fileProcessor, flatFile);
				}
			}
		}
	}

	private boolean isFileNotYetTransferredCompletely(File flatFile) {
		String []fileNameSplittedArray = flatFile.getName().split("\\.");
		return fileNameSplittedArray[fileNameSplittedArray.length - 1].equals("TMP");
	}

	private void processTMTAFlatFile(TMANFileProcessor fileProcessor, File flatFile) {
		if(fileProcessor.isFlatFileValid(flatFile)) {
			logger.info("Processing: " + flatFile.getName());
			
			try {
				fileProcessor.initReader(flatFile);
				fileProcessor.process(flatFile);
				moveFlatFileToProcessedFilesDirectory(fileProcessor.getProcessedFilesDirectory(), flatFile);
			} catch (Exception e) {
				try { moveFlatFileToInvalidFilesDirectory(fileProcessor.getInvalidFilesDirectory(), flatFile);
				} catch (IOException e1) {}
				logger.error(e.getMessage());
			} finally {
				logger.info("Processing Completed For: " + flatFile.getName());
				try { fileProcessor.shutdown(); } catch(IOException e){}
				flatFile.delete();
			}
		} else {
			try { moveFlatFileToInvalidFilesDirectory(fileProcessor.getInvalidFilesDirectory(), flatFile);
			} catch (IOException e1) {}
		}
	}
	
	private void moveFlatFileToProcessedFilesDirectory(String destination, File flatFile) throws IOException {
	    moveFlatFileToDestination(flatFile, destination);
	}
	
	private void moveFlatFileToInvalidFilesDirectory(String destination, File flatFile) throws IOException {
		moveFlatFileToDestination(flatFile, destination);
	}

	private void moveFlatFileToDestination(File flatFile,
			String destinationFolder) throws FileNotFoundException, IOException {
		InputStream inStream = new FileInputStream(flatFile);
		OutputStream outStream = new FileOutputStream(new File(destinationFolder + "\\" + flatFile.getName()));
		
		byte[] buffer = new byte[1024];
		int length;
	    while ((length = inStream.read(buffer)) > 0)
	    	outStream.write(buffer, 0, length);
	    
	    inStream.close();
	    outStream.close();
	}

	public List<TMANFileProcessor> getProcessors() {
		return processors;
	}

	public void setProcessors(List<TMANFileProcessor> processors) {
		this.processors = processors;
	}
}