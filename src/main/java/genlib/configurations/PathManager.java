package genlib.configurations;
import genlib.GenLib;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;


public class PathManager {
	private static final Logger LOG = Logger.getLogger(PathManager.class.getName());
	
	private static PathManager instance;
	
	/**
	 * Defaultny korenovy adresar 
	 */
	private File rootPath;
	/**
	 * Cesta k dodatocnym local suborom.
	 */
	private File localePath;
	
	/**
	 * Metoda ktora navrati instanciu PathManager. Pre unikatnost tejto instancie pre celu aplikaciu
	 * dodrziavame principy Singleton navrhoveho vzoru.
	 * @return Singleton instanciu PathManageru
	 */
	public static PathManager getInstance() {
		if (instance == null) {			
			instance = new PathManager();
			// Instance of this PathManager is universal for this run.
			instance.init();
		}
		return instance;
	}
	
	/**
	 * Inicializacia PathManageru s defaultnymi cestami. 
	 * Root zlozka je miesto kde sa nachadza aplikacia.
	 * Plugin zlozka je v root zlozke pod nazvom plugins.
	 * User zlozka je pocas behu aplikacie menitelna no defaultne je rovnaka ako root.
	 */
	public void init() {
		/*
		 *  Ziskame lokaciu triedy. Pri spustani v IDE vrati zlozku. Pri spustani z jar
		 *  zase cestu k jaru.
		 */
		
		URL url = GenLib.class.getProtectionDomain().getCodeSource().getLocation();		
		// 
		File _pRoot = new File(url.getFile());		
		if (_pRoot.isDirectory()) {
			rootPath = _pRoot;
		} else {
			rootPath = new File(_pRoot.getParent());
		}		
		
		// Ostatne cesty nastavime priamo z root
		localePath = new File(rootPath,"locales");
	}

	/**
	 * Getter pre root zlozku. Root zlozka je pociatkom alebo tiez miestom tejto aplikacie.
	 * Ostatne zlozky cerpaju priamo z nej
	 * @see #getPluginPath() 
	 * @return Root/korenova cesta/subor. 
	 */
	public File getRootPath() {			
		return rootPath;
	}
	
	public File getLocalePath() {
		return localePath;
	}
		
	
	
	
}
