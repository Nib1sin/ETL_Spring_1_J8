package com.efsoft.engineContable.etl.readers;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.efsoft.engineContable.etl.config.AppConfig;
import com.efsoft.engineContable.etl.pojo.GenericInputData;
import com.efsoft.engineContable.etl.reject.Interceptor;
import com.efsoft.engineContable.etl.reject.InvalidParseException;
import com.google.common.base.Strings;

public class CommonReader extends FlatFileItemReader<GenericInputData>{

	@Autowired
	private AppConfig appConfig;

	private static final Logger appLogger = LoggerFactory.getLogger(CommonReader.class);
	protected static final Logger rejectLogger = LoggerFactory.getLogger(Interceptor.class);
	protected final String ERR_CAMPO_OBBLIGATORIO = "Campo obbligatorio mancante - %s";
	protected final String ERR_VIOLAZIONE_PK = "Violazione chiave primaria flusso dati";
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private String fileName;

	public CommonReader(final String fileName) {
		this.fileName = fileName;
	}

	public void PostConstruct() throws Exception {

		appLogger.info("Reading file [" + fileName + "]");

		//
		this.setResource(appConfig.getIoManager().getFileResource(appConfig.getIoInputPath() + fileName));
		rejectLogger.error(fileName, "INIT");

		//
		final FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		XPath xPath = XPathFactory.newInstance().newXPath();

		final NodeList fieldNodeList = (NodeList) xPath
				.compile(String.format("//file[@name=\"%s\"]/fields/field", fileName))
				.evaluate(appConfig.getInputConfigurationXmlDoc(), XPathConstants.NODESET);

		String[] names = new String[fieldNodeList.getLength()];
		Range[] ranges = new Range[fieldNodeList.getLength()];
		for (int i = 0; i < fieldNodeList.getLength(); i++) {
			Node n = fieldNodeList.item(i);
			names[i] = n.getAttributes().getNamedItem("name").getNodeValue();
			ranges[i] = new Range(Integer.parseInt(n.getAttributes().getNamedItem("start").getNodeValue()),
					Integer.parseInt(n.getAttributes().getNamedItem("end").getNodeValue()));
		}

		tokenizer.setNames(names);
		tokenizer.setColumns(ranges);
		tokenizer.setStrict(true);

		this.setLineMapper(new LineMapper<GenericInputData>() {

			@Override
			public GenericInputData mapLine(String line, int lineNumber) throws Exception {

				GenericInputData result = null;

				try {

					FieldSet fs = tokenizer.tokenize(line);
					Map<String, Object> parsedData = new HashMap<>();

					for (int i = 0; i < fieldNodeList.getLength(); i++) {
						Node n = fieldNodeList.item(i);
						String name = n.getAttributes().getNamedItem("name").getNodeValue();
						String tJava = n.getAttributes().getNamedItem("tJava").getNodeValue();

						Boolean nullable = n.getAttributes().getNamedItem("nullable").getNodeValue() != null
								? Boolean.parseBoolean(n.getAttributes().getNamedItem("nullable").getNodeValue())
								: true;
						int lenght = Integer.parseInt(n.getAttributes().getNamedItem("len").getNodeValue());

						if (!nullable && !tJava.equals("String") && fs.readString(name).isEmpty())
							throw new Exception(String.format(ERR_CAMPO_OBBLIGATORIO, name));

						switch (tJava) {
							case "String":
								parsedData.put(
									name, !fs.readString(name).isEmpty() ? fs.readString(name) : (!nullable ? StringUtils.repeat(' ', lenght) : null)
								);
								break;
							case "Double":
								int scale = Integer.parseInt(n.getAttributes().getNamedItem("scale").getNodeValue());
								parsedData.put(
									name, (Double.parseDouble(fs.readString(name).replace(",", "")))
										/ Integer.parseInt("1" + Strings.repeat("0", scale))
								);
								break;
							case "Date":
								if (!fs.readString(name).isEmpty() && !fs.readString(name).equals("00000000")
										&& !fs.readString(name).equals("0001-01-01")) {
									parsedData.put(name, simpleDateFormat.parse(fs.readString(name)));
								} else {
									parsedData.put(name, null);
								}
								break;
							case "Integer":
								parsedData.put(
									name, !fs.readString(name).isEmpty() ? fs.readInt(name) : 0
								);
								break;
							case "Long":
								parsedData.put(
									name, !fs.readString(name).isEmpty() ? Long.parseLong(fs.readString(name).replace("EUR", "")) : 0
								);
								break;
							default:
								break;
						}
					}

					result = new GenericInputData(fileName, line, lineNumber++, parsedData);
				} catch (Exception e) {
					if(!line.isEmpty()){
						appLogger.error("{}|{}|{}", lineNumber, line, e.getMessage());
						rejectLogger.error(fileName, line);
						throw new InvalidParseException();	//Exception Custom
					}
				}

				return result;
			}
		});

	}


}
