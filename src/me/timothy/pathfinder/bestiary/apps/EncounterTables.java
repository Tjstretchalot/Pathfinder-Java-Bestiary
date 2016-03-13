package me.timothy.pathfinder.bestiary.apps;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import me.timothy.pathfinder.bestiary.models.ChallengeRating;
import me.timothy.pathfinder.bestiary.models.Creature;
import me.timothy.pathfinder.bestiary.models.Environment;
import me.timothy.pathfinder.bestiary.models.Temperature;
import me.timothy.pathfinder.bestiary.persisters.CRPersister;
import me.timothy.pathfinder.bestiary.persisters.CreaturePersister;
import me.timothy.pathfinder.bestiary.persisters.EnvironmentPersister;
import me.timothy.pathfinder.bestiary.persisters.PersisterFactory;
import me.timothy.pathfinder.bestiary.persisters.TemperaturePersister;
import me.timothy.pathfinder.bestiary.persisters.impl.sqlite.SqlitePersisterFactory;

/**
 * This generates an encounter table using the persister
 * model for the bestiary.
 * 
 * @author Timothy
 */
public class EncounterTables implements Runnable {
	private PersisterFactory persFactory;
	
	public static void main(String[] args) {
		SqlitePersisterFactory factory = new SqlitePersisterFactory();
		EncounterTables encTables = new EncounterTables(factory);
		encTables.run();
		factory.close();
	}
	
	public EncounterTables(PersisterFactory factory) {
		persFactory = factory;
	}
	
	@Override
	public void run() {
		CRPersister crPers = (CRPersister) persFactory.getPersisterForClass(ChallengeRating.class);
		EnvironmentPersister envPers = (EnvironmentPersister) persFactory.getPersisterForClass(Environment.class);
		CreaturePersister creaPers = (CreaturePersister) persFactory.getPersisterForClass(Creature.class);
		TemperaturePersister tempPers = (TemperaturePersister) persFactory.getPersisterForClass(Temperature.class);
		
		List<ChallengeRating> allowedCrs = Arrays.asList(
				crPers.fetchByName("1/4"),
				crPers.fetchByName("1/3"),
				crPers.fetchByName("1/2"),
				crPers.fetchByName("1"),
				crPers.fetchByName("2"),
				crPers.fetchByName("3")
				);
		List<Environment> allowedEnvs = Arrays.asList(
				envPers.fetchByName("plains")
				);
		List<Temperature> allowedTemps = Arrays.asList(
				tempPers.fetchByName("temperate")
				);
		
		
		List<Integer> allowedCrIds = new ArrayList<>();
		allowedCrs.forEach(cr -> allowedCrIds.add(cr.id));
		
		List<Integer> allowedEnvIds = new ArrayList<>();
		allowedEnvs.forEach(e -> allowedEnvIds.add(e.id));
		
		List<Integer> allowedTempIds = new ArrayList<>();
		allowedTemps.forEach(t -> allowedTempIds.add(t.id));
		
		List<Creature> creatures = creaPers.fetchByEnvironmentAndTemperature(allowedEnvIds, allowedTempIds);
		creatures = creatures.stream().filter(c -> allowedCrIds.contains(c.challengeRatingId)).collect(Collectors.toList());

		int max = 100 * (creatures.size() / 50 + 1); // flooring makes this 100, 200, 300, etc.
		String title = createTitleBy(allowedEnvs, allowedTemps, allowedCrs, max);
		
		String encTable = stringifyEncounterTable(title, max, getEncounterTable(max, creatures));
		
		System.out.println(encTable);
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Print? Y/N");
		if(!in.nextLine().equalsIgnoreCase("y")) {
			return;
		}
		final String toPrint = encTable;
		final String[] lines = toPrint.split("\n");
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(new Printable() {
			@Override
			public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
				g2d.translate(pf.getImageableX(), pf.getImageableY());
				
				int marginLeft = (int) Math.round(0.1 * pf.getImageableWidth());
//				int marginRight = (int) Math.round(0.044 * pf.getImageableWidth());
				int marginTop = (int) Math.round(0.036 * pf.getImageableHeight());
				int marginBottom = (int) Math.round(0.036 * pf.getImageableHeight());
//				int marginTop = 0;
//				int marginBottom = 0;
				int effectiveHeight = (int) Math.round(pf.getImageableHeight() - marginTop - marginBottom);
//				int effectiveWidth = (int) Math.round(pf.getImageableWidth() - marginLeft - marginRight);
				
				int lineHeight = g2d.getFontMetrics().getHeight();
				int linesPerPage = effectiveHeight / lineHeight;
				
				int linesStart = linesPerPage * page;
				if(linesStart > lines.length)
					return NO_SUCH_PAGE;
				
				for(int i = linesStart; i < linesStart + linesPerPage && i < lines.length; i++) {
					String line = lines[i];
					g2d.drawString(line, marginLeft, marginTop + (i - linesStart) * lineHeight);
				}
				
				return PAGE_EXISTS;
			}
			
		});
		if(job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException e1) {
				throw new RuntimeException(e1);
			}
		}
	}

	private String createTitleBy(List<Environment> allowedEnvs, List<Temperature> allowedTemps,
			List<ChallengeRating> allowedCrs, int max) {
		String title = "";
		boolean first = true;
		for(Temperature t : allowedTemps) {
			if(!first) 
				title += "/";
			else
				first = false;
			
			title += t.name;
		}
		title += " ";
		first = true;
		for(Environment e : allowedEnvs) {
			if(!first) 
				title += "/";
			else
				first = false;
			
			title += e.name;
		}
		
		ChallengeRating lowCr = findLowCr(allowedCrs);
		ChallengeRating highCr = findHighCr(allowedCrs);
		
		title = titleCase(title.trim());
		title += " CR " + lowCr.name + "-" + highCr.name + " ";
		title += "(roll d" + max + ")";
		return title;
	}

	// http://stackoverflow.com/a/1086134 - modified to include '/'
	private String titleCase(String input) {
		StringBuilder titleCase = new StringBuilder();
	    boolean nextTitleCase = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c) || c == '/') {
	            nextTitleCase = true;
	        } else if (nextTitleCase) {
	            c = Character.toTitleCase(c);
	            nextTitleCase = false;
	        }

	        titleCase.append(c);
	    }

	    return titleCase.toString();
	}

	private ChallengeRating findHighCr(List<ChallengeRating> crs) {
		ChallengeRating high = null;
		for(ChallengeRating cr : crs) {
			if(high == null || cr.totalExperience > high.totalExperience)
				high = cr;
		}
		return high;
	}

	private ChallengeRating findLowCr(List<ChallengeRating> crs) {
		ChallengeRating low = null;
		for(ChallengeRating cr : crs) {
			if(low == null || cr.totalExperience < low.totalExperience)
				low = cr;
		}
		return low;
	}

	static class CreatureEncounter implements Comparable<CreatureEncounter> {
		public Creature creature;
		public ChallengeRating cr;
		public int lowRoll;
		public int highRoll;
		
		public CreatureEncounter(Creature creature, ChallengeRating cr) {
			this.creature = creature;
			this.cr = cr;
		}

		@Override
		public int compareTo(CreatureEncounter o) {
			if(o.cr.equals(cr)) {
				return creature.name.compareTo(o.creature.name);
			}
			
			return cr.totalExperience - o.cr.totalExperience;
		}
	}
	
	private List<CreatureEncounter> getEncounterTable(int max, List<Creature> creatures) {
		CRPersister crPers = (CRPersister) persFactory.getPersisterForClass(ChallengeRating.class);
		
		List<CreatureEncounter> encounters = new ArrayList<>();
		creatures.forEach(c -> encounters.add(new CreatureEncounter(c, crPers.fetchByID(c.challengeRatingId))));
		
		Collections.sort(encounters);
		
		
		int each = max / encounters.size();
		int buffer = max - each * encounters.size();
		int cur = 1;
		
		for(CreatureEncounter ce : encounters) {
			ce.lowRoll = cur;
			cur += each - 1;
			if(buffer > 0) {
				cur++;
				buffer--;
			}
			ce.highRoll = cur;
			cur++;
		}
		return encounters;
	}

	private String stringifyEncounterTable(String title, int max, List<CreatureEncounter> ces) {
		StringBuilder result = new StringBuilder();
		
		int lowColWidth = 5;
		int highColWidth = 5;
		int nameColWidth = "Creature".length();
		int crWidth = 4;
		
		
		for(CreatureEncounter ce : ces) {
			nameColWidth = Math.max(ce.creature.name.length(), nameColWidth);
		}
		result.append(title).append("\n");
		result.append(printTopOrBottom(lowColWidth, highColWidth, nameColWidth, crWidth));
		result.append(printRow(lowColWidth, highColWidth, nameColWidth, crWidth, "Low", "High", "Creature", "CR"));
		result.append(printTopOrBottom(lowColWidth, highColWidth, nameColWidth, crWidth));
		for(CreatureEncounter ce : ces) {
			result.append(printRow(lowColWidth, highColWidth, nameColWidth, crWidth, "" + ce.lowRoll, "" + ce.highRoll, ce.creature.name, ce.cr.name));
		}
		result.append(printTopOrBottom(lowColWidth, highColWidth, nameColWidth, crWidth));
		return result.toString();
	}
	
	private String printTopOrBottom(int... widths) {
		StringBuilder result = new StringBuilder();
		for(int width : widths) {
			result.append("|");
			IntStream.range(0, width).forEach(i -> result.append("-"));
		}
		
		result.append("|\n");
		return result.toString();
	}
	
	private String printRow(int lowColWidth, int highColWidth, int nameColWidth, int crWidth, String low, String high, String name, String cr) {
		StringBuilder result = new StringBuilder();
		result.append("|");
		result.append(low);
		IntStream.range(0, lowColWidth - low.length()).forEach(i -> result.append(' '));
		result.append('|');
		result.append(high);
		IntStream.range(0, highColWidth - high.length()).forEach(i -> result.append(' '));
		result.append("|");
		result.append(name);
		IntStream.range(0, nameColWidth - name.length()).forEach(i -> result.append(' '));
		result.append('|');
		result.append(cr);
		IntStream.range(0, crWidth - cr.length()).forEach(i -> result.append(' '));
		result.append("|\n");
		return result.toString();
	}
}
