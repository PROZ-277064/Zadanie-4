package proz;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ExchangeRatesSeries")
public class ExchangeRatesSeries {
	@XmlElement(name = "Table")
	private String Table;

	@XmlElement(name = "Rates")
	private Rates rates;

	public static class Rates {

		@XmlElement(name = "Rate")
		private ArrayList<Rate> listRate = new ArrayList<Rate>();

		public static class Rate {
			@XmlElement(name = "Mid")
			private String Mid;
			@XmlElement(name = "Ask")
			private String Ask;
		}
	}

	private double getSum() {
		double sum = 0;
		for (Rates.Rate rate : rates.listRate) {
			if (Table.equals("C"))
				sum += Double.parseDouble(rate.Ask);
			else
				sum += Double.parseDouble(rate.Mid);
		}
		return sum;
	}
	
	public double getAverage() {
		return Math.round(getSum()/rates.listRate.size() * 10000.0) / 10000.0;
	}
}
