This is a way to comfortably run proxy with multiple bundles.
 
 1. Make sure, that the adaptive proxy project is called "adaptive-proxy"
 2. Add adaptive-proxy and all bundles as a project dependency on the build path
 	Eclipse: Right click on this project -> Properties -> Java Build Path -> Projects -> Add
 3. In adaptive-proxy project, add "conf", "htdocs" and "plugins" directories as class folders
    Eclipse: Right click on adaptive-proxy project -> Properties -> Java Build Path -> Libraries -> Add Class Folder
 4. In each bundle, add "plugins" and "static" directories
    Eclipse: Right click on bundle project -> Properties -> Java Build Path -> Libraries -> Add Class Folder
    
Do this once, then start proxy via sk.fiit.peweproxy.Bootstrap 
    
Shortcomings:
	- variables.xml are overwritten -> last bundle wins
	- plugins_ordering from bundles is ignored
	
    
 