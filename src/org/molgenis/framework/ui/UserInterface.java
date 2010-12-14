package org.molgenis.framework.ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.security.Login;
import org.molgenis.util.EmailService;
import org.molgenis.util.Entity;
import org.molgenis.util.FileLink;
import org.molgenis.util.Tuple;

/**
 * The root screen for any MOLGENIS application.
 * <p>
 * The UserInterface manages a Tree of Screens. A UserInterface is backed by
 * exactly one database (for persistent data) and one Login object (taking care
 * of authentication/authorization).
 */
public class UserInterface<E extends Entity> extends SimpleModel<E> implements ScreenController<E, UserInterface<E>>
{
	public static String MOLGENIS_TEMP_PATH = "molgenis_temp";
	/** autogenerated */
	private static final long serialVersionUID = 3108474555679524568L;
	/** */
	private static final transient Logger logger = Logger.getLogger(UserInterface.class.getSimpleName());
	/** The login * */
	private Login login;
	/** The email service used*/
	private EmailService emailService;
	/** The version used to generate this MOLGENIS*/
	private String version;

	/**
	 * Construct a user interface for a database.
	 * 
	 * @param login
	 *        for authentication/authorization
	 */
	public UserInterface(Login login)
	{
		super("molgenis_userinterface_root", null); // this is the root of the screen tree.
		//this.database = db;
		this.setLogin(login);
		this.setLabel("test");
		this.setController(this);
	}
	
	public UserInterface(Login login, EmailService email)
	{
		this(login);
		this.setLogin(login);
		this.setLabel("test");
		this.setController(this);
		this.setEmailService(email);
	}
	
	

	/**
	 * Retrieve the current login
	 * 
	 * @return Login
	 */
	public Login getLogin()
	{
		return login;
	}

	/**
	 * Set the Login object that is used for authentication.
	 * 
	 * @param login
	 */
	public void setLogin( Login login )
	{
		this.login = login;
	}

	/**
	 * Retrieve the database that is used by this user interface.
	 * 
	 * @return db the database
	 */
//	public Database getDatabase()
//	{
//		return database;
//	}

//	public void setDatabase(Database database)
//	{
//		logger.info("replacing database "+this.database+" with "+database);
//		this.database = database;
//	}
	
	@Override
	public FileLink getTempFile() throws IOException
	{
		//File temp = new File("d:\\Temp\\"+System.currentTimeMillis());
		//String tempDir = System.getProperty("java.io.tmpdir");
		File temp = File.createTempFile(MOLGENIS_TEMP_PATH, "");
		logger.debug("create temp file: "+temp);
		return new FileLink(temp, "download/"+temp.getName());
	}
	
	/**
	 * Convenience method that delegates an event to the controller of the
	 * targetted screen.
	 * 
	 * @param db reference to the database
	 * @param request
	 *        with the event
	 */
	public void handleRequest( Database db, Tuple request )
	{
		logger.debug("delegating handleRequest(" + request.toString() + ")");
		String screen = request.getString(ScreenModel.INPUT_TARGET);
		
		//action for me?
		if(screen != null && screen.equals(this.getName()))
		{
			if (request.getString("select") != null)
			{
				 // the screen to select
			    ScreenModel<?> selected = this.get(request.getString("select") );
			    
			    // now we must make sure that alle menu's above select 'me'
			    ScreenModel<?> currentParent = selected.getParent();
			    ScreenModel<?> currentChild = selected;
			    while(currentParent != null)
			    {
			     if(currentParent instanceof MenuModel<?>)
			     {
			      ((MenuModel<?>)currentParent).setSelected(currentChild.getName());
			     }
			     currentChild = currentParent;
			     currentParent = currentParent.getParent();
			    }
			}
			return;
		}
		
		//delegate
		ScreenModel<?> target = get(screen);
		if( target != null)
		{
			if(!target.getController().equals(this))
					target.getController().handleRequest(db, request);
		}
		else
			logger.debug("handleRequest("+request+"): no request needs to be handled");

	}

	/**
	 * Convenience method that delegates the refresh to its ScreenController.
	 */
	public void reload(Database db)
	{
		for(ScreenModel<?> s: this.getChildren())
		{
			s.getController().reload(db);
		}
		
//		if(this.getController() != this)
//		{
//			this.getController().reload(db);
//		}
//		else
//		{
//			//refresh whole selected subtree
//			if(this.getSelected() != null)
//				this.getSelected().getController().reload(db);
//		}
	}

	@Override
	public Templateable getScreen()
	{
		// TODO Auto-generated method stub
		return this;
	}
	
	@Override
	public String getViewName()
	{
		return this.getClass().getSimpleName();
	}

	@Override
	public void handleRequest(Database db, Tuple request, PrintWriter out) {
		this.handleRequest(db, request);

	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public EmailService getEmailService()
	{
		return emailService;
	}

	public void setEmailService(EmailService emailService)
	{
		this.emailService = emailService;
	}

	@Override
	public boolean isVisible()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getViewTemplate()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		
	}
}
