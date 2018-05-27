package proz;

import java.io.StringReader;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Path("/exchangerates/rates")
public class AverageExchangeRateGetter {

	private String getAverageFromNBP(String table, String code, String topCount) {
		URI uri = UriBuilder
				.fromUri("http://api.nbp.pl/api/exchangerates/rates/" + table + "/" + code + "/last/" + topCount)
				.build();

		String answer = ((ClientBuilder.newClient()).target(uri)).request().accept(MediaType.TEXT_XML)
				.get(String.class);

		ExchangeRatesSeries convertedAnswer = null;

		try {
			JAXBContext context = JAXBContext.newInstance(ExchangeRatesSeries.class);
			convertedAnswer = (ExchangeRatesSeries) context.createUnmarshaller().unmarshal(new StringReader(answer));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		if (convertedAnswer != null)
			return String.valueOf(convertedAnswer.getAverage());

		return "error";
	}

	@GET
	@Path("{table}/{code}/{topCount}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAveragePlainText(@PathParam("table") String table, @PathParam("code") String code,
			@PathParam("topCount") String topCount) {
		return getAverageFromNBP(table, code, topCount);
	}

	@GET
	@Path("{table}/{code}/{topCount}")
	@Produces(MediaType.TEXT_XML)
	public String getAverageXML(@PathParam("table") String table, @PathParam("code") String code,
			@PathParam("topCount") String topCount) {
		return "<?xml version=\"1.0\"?>" + "<Avg>" + getAverageFromNBP(table, code, topCount).toString() + "</Avg>";
	}

	@GET
	@Path("{table}/{code}/{topCount}")
	@Produces(MediaType.TEXT_HTML)
	public String getAverageHTML(@PathParam("table") String table, @PathParam("code") String code,
			@PathParam("topCount") String topCount) {
		return "<html><body>" + getAverageFromNBP(table, code, topCount) + "</body></html>";
	}

	@GET
	@Path("{table}/{code}/{topCount}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAverageJSON(@PathParam("table") String table, @PathParam("code") String code,
			@PathParam("topCount") String topCount) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("Avg", getAverageFromNBP(table, code, topCount));
		return (builder.build()).toString();
	}

}
