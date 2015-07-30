package genlib.locales;

/**
 * Class with static resources (texts).
 * 
 * @author Lukas Surin
 *
 */
public final class PermMessages {

	private PermMessages() {
	}

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
	public static final String _security_exc = "Security exception, can't create directories";
	public static final String _exc_badins = "Bad type of instances";
	public static final String _exc_nonwsupp = "No support without using weka";
	// Plugin load
	public static final String _plug_type_err = "Bad type of loaded plugin %s. Move it to correct dir.";
	public static final String _c_plug_loaded = "Number of %s plugins loaded : %s.";
	public static final String _c_class_loaded = "Number of %s classes recognized : %s.";
	public static final String _class_duplkey = "Duplicate key %s for classes %s, %s.";
	// population init
	public static final String _s_popinit = "Loading of available population initializators";
	public static final String _popclass_loaded = "Class with population initializator %s is recognized. Usage parameter registered as %s.";
	// generator init
	public static final String _gen_dummy = "Dummy generator which creates individuals consisting of leaves.";
	public static final String _s_geninit = "Loading of available individual generators.";
	public static final String _genclass_loaded = "Class with individual generator %s is recognized. Usage parameter registered as %s.";
	// operator init
	public static final String _s_operinit = "Loading of available operators.";
	public static final String _operclass_loaded = "Class with operator %s is recognized. Usage parameter registered as %s.";
	// fitness init
	public static final String _s_fitinit = "Loading of available fitness functions";
	public static final String _fitclass_loaded = "Class with fitness function %s is recognized. Usage parameter registered as %s.";
	// selector init
	public static final String _s_selinit = "Loading of available selectors";
	public static final String _selclass_loaded = "Class with selector %s is recognized. Usage parameter registered as %s.";
	// split init
	public static final String _s_splitinit = "Loading of available split criterias";
	public static final String _splitclass_loaded = "Class with splitcriteria %s is recognized. Usage parameter registered as %s.";
	// split init
	public static final String _s_classifiersinit = "Loading of available classifiers";
	public static final String _classifierclass_loaded = "Class with classifier %s is recognized. Usage parameter registered as %s.";
	// population container init
	public static final String _s_popcontinit = "Loading of available population containers";
	public static final String _populationcontainer_loaded = "Class with population container %s is recognized. Usage parameter registered as %s.";
	// formats
	public static final String _blank_param = "x";
	public static final String _param_format = "%s,%s";
	public static final String _fit_format = "%s %s";

	// Default operator text
	public static final String _def_mut = "dtM 0";
	public static final String _def_xover = "dtX 0";
	public static final String _def_popInit = "dtX 0";
}
