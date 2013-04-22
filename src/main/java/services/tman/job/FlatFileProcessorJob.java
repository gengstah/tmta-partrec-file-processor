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
	private Logger logger = Logger.getLogger(getClass());
	private List<TMANFileProcessor> processors;
	
	/**
	 * Default constructor
	 */
	public FlatFileProcessorJob() { }

	/**
	 * Execute the actual job. The job data map will already 
	 * have been applied as bean property values by execute. 
	 * The contract is exactly the same as for the standard 
	 * Quartz execute method.
	 * 
	 * @param jec The {@link org.quartz.JobExecutionContext}
	 * @throws JobExecutionException When the application
	 * encounters a problem with job execution or scheduling
	 */
	@Override
	protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
		for(TMANFileProcessor fileProcessor : processors) {
			logger = Logger.getLogger(fileProcessor.getClass());
			for(String directoryName : fileProcessor.getInputDirectoriesToProcessedFilesDirectories().keySet()) {
				File input = new File(directoryName);
				String []flatFileNames = input.list();
				
				for(String flatFileName : flatFileNames) {
					File flatFile = new File(directoryName + "\\" + flatFileName);
					if(flatFile.isDirectory()) continue;
					if(isFileNotYetTransferredCompletely(flatFile)) continue;
					
					processTMTAFlatFile(fileProcessor, flatFile, directoryName);
				}
			}
		}
	}

	private boolean isFileNotYetTransferredCompletely(File flatFile) {
		String []fileNameSplittedArray = flatFile.getName().split("\\.");
		return fileNameSplittedArray[fileNameSplittedArray.length - 1].equals("TMP");
	}

	private void processTMTAFlatFile(TMANFileProcessor fileProcessor, File flatFile, String inputDirectory) {
		if(fileProcessor.isFlatFileValid(flatFile)) {
			logger.info("Processing: " + flatFile.getName());
			
			try {
				fileProcessor.initReader(flatFile);
				fileProcessor.process(flatFile);
				moveFlatFileToDestination(flatFile,
						fileProcessor
							.getInputDirectoriesToProcessedFilesDirectories()
							.get(inputDirectory));
			} catch (Exception e) {
				try { moveFlatFileToDestination(flatFile,
						fileProcessor
							.getInputDirectoriesToInvalidFilesDirectories()
							.get(inputDirectory));
				} catch (IOException e1) {}
				logger.error(e.getMessage());
			} finally {
				logger.info("Processing Completed For: " + flatFile.getName());
				try { fileProcessor.shutdown(); } catch(IOException e){}
				flatFile.delete();
			}
		} else {
			try { moveFlatFileToDestination(flatFile,
					fileProcessor
						.getInputDirectoriesToInvalidFilesDirectories()
						.get(inputDirectory));
			} catch (IOException e1) {}
		}
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

	/**
	 * Retrieves the list of {@link services.tman.control.TMANFileProcessor}
	 * 
	 * @return The list of {@link services.tman.control.TMANFileProcessor}
	 */
	public List<TMANFileProcessor> getProcessors() {
		return processors;
	}

	/**
	 * Set the list of {@link services.tman.control.TMANFileProcessor}
	 * 
	 * @param processors The list of {@link services.tman.control.TMANFileProcessor}
	 */
	public void setProcessors(List<TMANFileProcessor> processors) {
		this.processors = processors;
	}
}