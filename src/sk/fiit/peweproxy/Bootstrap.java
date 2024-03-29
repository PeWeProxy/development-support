package sk.fiit.peweproxy;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;

import rabbit.proxy.ProxyStarter;
import sk.fiit.peweproxy.utils.XMLFileParser;

public class Bootstrap {
	private static final String PROXY_PROJECT_NAME = "adaptive-proxy";
	
	private static void copyBasicProxyDirectory(String basicDir, Collection<File> baseCPEntries) throws IOException {
		File localConfig = new File(basicDir);
		
		if(localConfig.exists()) {
			if(!FileUtils.rm_r(localConfig)) {
				throw new IllegalStateException("Could not delete a directory: " + localConfig.getAbsolutePath());
			}
		}

		if(!localConfig.mkdir()) {
			throw new IllegalStateException("Could not create a directory: " + localConfig.getAbsolutePath());
		}
			
		File discoveredDirectory = null;
		
		for (File file : baseCPEntries) {
			if(file.getAbsolutePath().endsWith(PROXY_PROJECT_NAME + File.separator + basicDir)) {
				FileUtils.cp_r(file, localConfig);
				discoveredDirectory = file;
				break;
			}
		}
		
		if(discoveredDirectory != null) {
			baseCPEntries.remove(discoveredDirectory);
		}
	}
	
	private static Collection<File> loadBaseCPEntries() throws IOException {
		Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("");
		List<File> baseCPEntries = new LinkedList<File>();
		while(resources.hasMoreElements()) {
			baseCPEntries.add(new File(resources.nextElement().getFile()));
		}
		
		return baseCPEntries;
	}
	

	private static void discoverAndCopyBundlePlugins(Collection<File> baseCPEntries) throws IOException {
		File discoveredDirectory = null;
		
		for (File file : baseCPEntries) {
			if(file.getAbsolutePath().endsWith("plugins")) {
				File[] plugins = file.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return !("plugins_ordering".equals(name) ||
								 "variables.xml".equals(name) ||
								 name.endsWith(".dtd"));
					}
				});
				
				for (File plugin : plugins) {					
					FileUtils.cp_r(plugin, new File("plugins" + File.separator + plugin.getName()));
				}				
				discoveredDirectory = file;
			}
		}
		
		if(discoveredDirectory != null) {
			baseCPEntries.remove(discoveredDirectory);
		}
	}
	
	private static void discoverAndCopyBundleAssets(Collection<File> baseCPEntries) throws IOException {
		File discoveredDirectory = null;
		
		for (File file : baseCPEntries) {
			if(file.getAbsolutePath().endsWith("static")) {
				
				FileUtils.cp_r(file, new File("htdocs/public"));
				discoveredDirectory = file;
			}
		}
		
		if(discoveredDirectory != null) {
			baseCPEntries.remove(discoveredDirectory);
		}
	}
	
	private static void createLogDirectory() {
		File logDir = new File("logs");
		if(logDir.exists()) {
			if(!FileUtils.rm_r(logDir)) {
				throw new IllegalStateException("Could not delete a directory: " + logDir.getAbsolutePath());
			}
		}

		if(!logDir.mkdir()) {
			throw new IllegalStateException("Could not create a directory: " + logDir.getAbsolutePath());
		}
	}
	
	private static void discoverAndMergeVariables(Collection<File> baseCPEntries) throws IOException {
		Document globalDocument = XMLFileParser.parseFile(new File("common/variables.xml"));
		
		for (File file : baseCPEntries) {
			if(file.getAbsolutePath().endsWith("plugins")) {
				File[] variablesFiles = file.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return ("variables.xml".equals(name));
					}
				});
				
				for (File variablesFile : variablesFiles){
					Document localDocument = XMLFileParser.parseFile(variablesFile.getAbsoluteFile());
					
					XMLUtils.appendVariables(globalDocument, localDocument);
				}
			}
		}
		XMLUtils.writeDocument(globalDocument);
	}
	
	private static void copyCommonFiles() throws IOException {
		FileUtils.cp(new File("common/plugins_ordering"), new File("plugins/plugins_ordering"));
	}

	public static void main(String[] args) throws Exception {
		Collection<File> baseCPEntries = loadBaseCPEntries();
		createLogDirectory();
		copyBasicProxyDirectory("conf", baseCPEntries);
		copyBasicProxyDirectory("htdocs", baseCPEntries);
		copyBasicProxyDirectory("plugins", baseCPEntries);
		
		discoverAndMergeVariables(baseCPEntries);
		
		discoverAndCopyBundlePlugins(baseCPEntries);
		discoverAndCopyBundleAssets(baseCPEntries);
		
		copyCommonFiles();
		
		ProxyStarter.main(args);
	}
}
