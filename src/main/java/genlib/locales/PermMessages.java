package genlib.locales;

public interface PermMessages {
	
	// Application msgs
	public static final String _app_started = "Application started";
	public static final String _app_ending = "Application ending";
	public static final String _app_ended = "Application ended";
	public static final String _app_name = "GenLib";
	// Argument msgs
	public static final String _arg_pars = "Main method argument parsing";
	public static final String _arg_ndef = "Parameter %s not defined!";
	// Localization/ResourceBundle msgs
	public static final String _loc_changed = "Localization changed to lang_%s";
	public static final String _loc_noval = "Message with id=%s is not defined in localization file";
	// Config msgs
	public static final String _cfg_saved = "Configuration was succesfully saved";
	public static final String _cfg_err_save = "Problem occured when saving property file. Exception : %s";
	public static final String _cfg_loaded = "Configuration was succesfully loaded. All missing values are going to be set.";
	public static final String _cfg_file_miss = "Config file does not exist. New one will be created";
	public static final String _cfg_err_load = "Problem occured when loading property file. Exception : %s";
	public static final String _cfg_set = "Missing values were succesfully set.";
	// Exception errors
	public static final String _exc_badins = "Bad type of instances";
	public static final String _exc_nonwsupp = "No support without using weka";
	// Default operator text
	public static final String _def_mut = "DEFAULT=0";
	public static final String _def_xover = "DEFAULT=0";
	
}
