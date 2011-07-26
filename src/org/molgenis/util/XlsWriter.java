package org.molgenis.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


public class XlsWriter 
{
	
	/** to send log messages on progress to */
	private static final transient Logger logger = Logger.getLogger(CsvWriter.class.getSimpleName());
	/** writer the output is written to */
	protected PrintWriter writer = null;
	/** separator used to separate columns, default "\t" */
	private String separator = "\t";
	/** separator used to separate lists, default "|" */
	private String listSeparator = "|";
	/** number of rows written */
	private int count = 0;
	/** value to use for missing/null values such as "NULL" or "NA", default "" */
	private String missingValue = "";
	/** headers to be written out */
	private List<String> headers = new ArrayList<String>();

	//BIG TODO : actually implement XLS download NOT csv, or include Joeri's function?

	public XlsWriter(PrintWriter writer)
	{
		this.writer = writer;
	}
	/**
	 * Construct the Writer, wrapping another writer.
	 */
	public XlsWriter(PrintWriter writer, List<String> headers)
	{
		this(writer);
		this.headers = headers;
	}

	

	/**
	 * Write out an XGAP matrix. The inputs can be retrieved from any
	 * implementation of the XGAP matrix interface class.
	 * 
	 * @param rowNames
	 * @param colNames
	 * @param elements
	 */
	public void writeMatrix(List<String> rowNames, List<String> colNames, Object[][] elements)
	{
		logger.info("writeMatrix called");
		String cols = "";
		for (String col : colNames)
		{
			cols += "\t" + col;
		}
		writer.println(cols);
		logger.info("printing: " + cols);
		for (int rowIndex = 0; rowIndex < rowNames.size(); rowIndex++)
		{
			String row = rowNames.get(rowIndex);
			for (int colIndex = 0; colIndex < colNames.size(); colIndex++)
			{
				if (elements[rowIndex][colIndex] == null)
				{
					row += "\t";
				}
				else
				{
					row += "\t" + elements[rowIndex][colIndex];
				}
			}
			writer.println(row);
			logger.info("printing: " + row);
		}
	}

	/**
	 * Write the header.
	 */
	public void writeHeader()
	{
		for (int i = 0; i < headers.size(); i++)
		{
			if (i < headers.size() - 1)
			{
				writer.print(headers.get(i) + separator);
			}
			else
			{
				writer.print(headers.get(i));
			}
		}
		writer.println();
	}

	/**
	 * Write a row to stream.
	 * 
	 * @param e
	 *            Entity to be written.
	 */
	public void writeRow(Entity e)
	{
		boolean first = true;
		for (String col : headers)
		{
			//print separator unless first element
			if (first)
			{
				first = false;
			}
			else
			{
				writer.print(separator);
			}
			//print value
			writeValue(e.get(col));

		}
		//newline
		writer.println();
		// writer.println(e.getValues(separator));
		if (++count % 10000 == 0) logger.debug("wrote line " + count + ": " + e);
	}

	/**
	 * Write a row to stream.
	 * 
	 * @param t
	 *            Tuple to be written.
	 */
	public void writeRow(Tuple t)
	{
		boolean first = true;
		for (String col : headers)
		{
			//print separator unless first element
			if (first)
			{
				first = false;
			}
			else
			{
				writer.print(separator);
			}
			//print value
			writeValue(t.getObject(col));
		}
		writer.println();
		if (count++ % 10000 == 0) logger.debug("wrote tuple to line " + count + ": " + t);
	}
	
	/**
	 * Write a row to stream
	 * 
	 * @param values
	 */
	public void writeValue(Object object)
	{
		if (object == null)
		{
			writer.print(this.missingValue);
		}

		else
		{
			if (object instanceof List<?>)
			{
				List<?> list = (List<?>) object;
				for (int i = 0; i < list.size(); i++)
				{
					// FIXME, what about escaping???
					if (i != 0) writer.print(listSeparator);
					if (list.get(i) != null)
					{
						writer.print(list.get(i).toString());
					}
					else
					{
						writer.print(this.getMissingValue());
					}
				}

			}
			else
			{
				//writer.print(StringEscapeUtils.escapeCsv(object.toString().trim().replace("\n", "")));
				writer.print(StringEscapeUtils.escapeCsv(object.toString()));
			}
		}

	}

	/**
	 * Get the String that is used for missing or null values, default 'NA'.
	 */
	public String getMissingValue()
	{
		return missingValue;
	}

	/**
	 * Set the String that is used for missingValues such as null, default 'NA'.
	 * 
	 * @param missingValue
	 *            new missing value String.
	 */
	public void setMissingValue(String missingValue)
	{
		this.missingValue = missingValue;
	}

	/**
	 * Get the separator used to separate columns that are outputed, default
	 * '\t'.
	 */
	public String getSeparator()
	{
		return separator;
	}

	/**
	 * Set the String that is used to separate columns that are outputed,
	 * default '\t'.
	 * 
	 * @param separator
	 *            new separator String.
	 */
	public void setSeparator(String separator)
	{
		this.separator = separator;
	}

	/** Close the writer */
	public void close()
	{
		writer.flush();
		writer.close();

	}

	public String getListSeparator()
	{
		return listSeparator;
	}

	public void setListSeparator(String listSeparator)
	{
		this.listSeparator = listSeparator;
	}

	public void setHeaders(List<String> fields)
	{
		this.headers = fields;
	}

	public List<String> getHeaders()
	{
		return this.headers;
	}

	public void writeSeparator()
	{
		this.writer.print(this.getSeparator());
	}

	public void writeEndOfLine()
	{
		this.writer.println();
	}
}