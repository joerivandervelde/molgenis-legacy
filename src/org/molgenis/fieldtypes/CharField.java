package org.molgenis.fieldtypes;

import org.molgenis.framework.ui.html.HtmlInput;
import org.molgenis.framework.ui.html.HtmlInputException;
import org.molgenis.framework.ui.html.StringInput;
import org.molgenis.model.MolgenisModelException;

public class CharField extends FieldType
{
	@Override
	public String getJavaAssignment(String value) throws MolgenisModelException
	{
		if(value == null || value.equals("") ) return "null";
		return "\""+value+"\"";
	}
	
	@Override
	public String getJavaPropertyDefault() throws MolgenisModelException
	{
		return getJavaAssignment(f.getDefaultValue());
	}

	@Override
	public String getJavaPropertyType() throws MolgenisModelException
	{
		return "String";
	}

	@Override
	public String getMysqlType() throws MolgenisModelException
	{
		return "CHAR("+f.getVarCharLength()+")";
	}
	
	@Override
	public String getOracleType() throws MolgenisModelException
	{
		return "CHAR("+f.getVarCharLength()+")";
	}

	@Override
	public String getHsqlType() throws MolgenisModelException
	{
		return "CHAR("+f.getVarCharLength()+")";
	}
	@Override
	public String getXsdType() throws MolgenisModelException
	{
		// TODO Auto-generated method stub
		return "string";
	}

	@Override
	public String getFormatString()
	{
		return "%s";
	}

	@Override
	public HtmlInput createInput(String name, String xrefEntityClassName) throws  HtmlInputException
	{
		return new StringInput(name);
	}

	@Override
	public String getCppPropertyType() throws MolgenisModelException
	{
		return "string";
	}

	@Override
	public String getCppJavaPropertyType()
	{
		return "Ljava/lang/String;";
	}

}
