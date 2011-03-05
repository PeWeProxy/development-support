This is a way to comfortably run proxy with multiple bundles.
 
 1. Make sure, that the adaptive proxy project is called "adaptive-proxy"
 2. Add adaptive-proxy and all bundles as a project dependency on the build path
 	Eclipse: Right click on this project -> Properties -> Java Build Path -> Projects -> Add
 3. In adaptive-proxy project, add "conf", "htdocs" and "plugins" directories as class folders
    Eclipse: Right click on adaptive-proxy project -> Properties -> Java Build Path -> Libraries -> Add Class Folder
 4. In each bundle, add "plugins" and "static" directories as class folders
    Eclipse: Right click on bundle project -> Properties -> Java Build Path -> Libraries -> Add Class Folder
    
Do this once, then start proxy via sk.fiit.peweproxy.Bootstrap 

Notes:
	- Variables are now merged from each bundle into one file. To avoid name conflicts, please respect this
	  naming convention for variables: 
	  
	  <variable name="bundle_name:variable_name">...</variable>

Shortcomings:
	- We need a way to merge plugins_ordering from multiple bundles. 
	  For now, these are copied from this project's "common" directory.