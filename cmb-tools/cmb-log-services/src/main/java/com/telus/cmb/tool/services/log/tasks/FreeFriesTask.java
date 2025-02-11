package com.telus.cmb.tool.services.log.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.tool.services.log.service.EmailService;

public class FreeFriesTask extends BaseTask {

	private static Logger logger = Logger.getLogger(FreeFriesTask.class);

	private static int MAGIC_NUMBER = 12;

	@Autowired
	private EmailService emailService;

	@Override
	public void run() {

		GameStat gameStat = getStats(yesterday());
		if (gameStat.getThrees() < 0) {
			logger.info("No raptors game last night.");
		} else {
			logger.info("Free fries counter today = " + gameStat.getThrees());
			emailService.sendEmail(getEmails(), getEmailSubject(gameStat.getThrees()), getEmailBody(gameStat), "gorapsgo@telus.com", "GoRaptorsGo", true);
		}
	}

	@Override
	public String getDescription() {
		return "Let's Go Raptors!";
	}

	private String yesterday() {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		return "" + year + String.format("%02d", month) + String.format("%02d", day);
	}

	private GameStat getStats(String date) {
		
		GameStat gameStat = new GameStat();
		gameStat.setThrees(-1);
		String gameId = getGameId(date);

		if (gameId != null) {
			try {
				String url = "http://www.espn.com/nba/boxscore?gameId=" + gameId;
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", "Mozilla/5.0");
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					if (inputLine.contains("boxscore-tabs game-package-box-score")) {
						int raps1Idx = inputLine.indexOf("Raptors");
						int raps2Idx = inputLine.indexOf("Raptors", raps1Idx + 1);
						int teamIdx = inputLine.indexOf("TEAM", raps2Idx);
						int treysIdx = inputLine.indexOf("3pt", teamIdx);
						int treysStartId = inputLine.indexOf(">", treysIdx);
						int treysEndId = inputLine.indexOf("-", treysStartId);
						String treys = inputLine.substring(treysStartId + 1, treysEndId);
						if (StringUtils.isNumeric(treys)) {
							gameStat.setThrees(Integer.parseInt(treys));
						}
						break;
					} else if (inputLine.contains("final-score")) {
						int firstFSIdx = inputLine.indexOf("final-score");
						int secondFSIdx = inputLine.indexOf("final-score", firstFSIdx + 1);
						int thirdFSIdx = inputLine.indexOf("final-score", secondFSIdx + 1);
						int torIdx = inputLine.indexOf("TOR");
						int firstScoreStartIdx = inputLine.indexOf(">", secondFSIdx + 1);
						int firstScoreEndIdx = inputLine.indexOf("<", firstScoreStartIdx + 1);
						int secondScoreStartIdx = inputLine.indexOf(">", thirdFSIdx + 1);
						int secondScoreEndIdx = inputLine.indexOf("<", secondScoreStartIdx + 1);
						String firstScore = inputLine.substring(firstScoreStartIdx + 1, firstScoreEndIdx);
						String secondScore = inputLine.substring(secondScoreStartIdx + 1, secondScoreEndIdx);
						if (torIdx > secondFSIdx) { 
							gameStat.setRapsPts(Integer.parseInt(secondScore));
							gameStat.setOpponentPts(Integer.parseInt(firstScore));
						} else {
							gameStat.setRapsPts(Integer.parseInt(firstScore));
							gameStat.setOpponentPts(Integer.parseInt(secondScore));
						}
					}
				}
				in.close();
			} catch (Exception e) {
				// Do nothing.
			}
		}

		return gameStat;
	}
	
	private String getGameId(String date) {

		String gameId = null;
		String url = "http://www.espn.com/nba/scoreboard/_/date/" + date;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("Toronto Raptors")) {
					int rapsIdx = inputLine.indexOf("Toronto Raptors");
					int uidIdx = inputLine.indexOf("uid", rapsIdx);
					int gameStartId = inputLine.indexOf("e:", uidIdx);
					int gameEndId = inputLine.indexOf("~", gameStartId);
					gameId = inputLine.substring(gameStartId + 2, gameEndId);
					break;
				}
			}
			in.close();
		} catch (Exception e) {
			// Do nothing.
		}

		return gameId;
	}

	private List<String> getEmails() {
		List<String> emails = new ArrayList<String>();
		emails.add("wilson.cheong@telus.com");
		return emails;
	}

	private String getEmailSubject(int numOTreys) {
		return numOTreys >= MAGIC_NUMBER ? "Free fries today!" : "No free fries today :(";
	}

	private String getEmailBody(GameStat gameStat) {

		String friesBody = "<div align='center'>" + "<h2>GET YO FREE FRIES!!!</h2>"
				+ "<img height='400' width='400' src='https://smartcanucks.ca/wp-content/uploads/2018/10/Screen-Shot-2018-10-15-at-1.37.37-PM.png'>"
				+ "<h3>RAPS made <div style='font-size:xx-large;'>" + gameStat.getThrees() + "</div> threes last night!<br/><br/>";
		friesBody += (gameStat.getRapsPts() > gameStat.getOpponentPts()) ? "and won " : "but lost ";
		friesBody += gameStat.getRapsPts() + " to " + gameStat.getOpponentPts();
		friesBody += "</h3></div>";
		
		String noFriesBody = "<div align='center'>" + "<h2>no free fries today &#128542;</h2>"
				+ "<img width='400' src='https://media1.tenor.com/images/1afa851d9ae7e412a04f3d2e553d9269/tenor.gif?itemid=5207999'>"
				+ "<h3>Raps <u>only</u> made <div style='font-size:xx-large;'>" + gameStat.getThrees() + "</div> threes last night...<br/><br/>";
		noFriesBody += (gameStat.getRapsPts() > gameStat.getOpponentPts()) ? "but won " : "and lost ";
		noFriesBody += gameStat.getRapsPts() + " to " + gameStat.getOpponentPts();
		noFriesBody += "</h3></div>";

		return gameStat.getThrees() >= MAGIC_NUMBER ? friesBody : noFriesBody;
	}

	public static void main(String[] args) {
		FreeFriesTask task = new FreeFriesTask();
		System.out.println(task.yesterday());
		GameStat gameStat = task.getStats(task.yesterday());
		System.out.println(gameStat.getThrees());
		System.out.println(gameStat.getRapsPts());
		System.out.println(gameStat.getOpponentPts());
		System.out.println(task.getEmailBody(task.getStats("20190210"))); // Fries + Win
		System.out.println(task.getEmailBody(task.getStats("20190125"))); // Fries + Loss
		System.out.println(task.getEmailBody(task.getStats("20190205"))); // No Fries + Win
		System.out.println(task.getEmailBody(task.getStats("20190131"))); // No Fries + Loss
	}
	
	private class GameStat {
		private int threes;

		public int getThrees() {
			return threes;
		}

		public void setThrees(int threes) {
			this.threes = threes;
		}

		public int getRapsPts() {
			return rapsPts;
		}

		public void setRapsPts(int rapsPts) {
			this.rapsPts = rapsPts;
		}

		public int getOpponentPts() {
			return opponentPts;
		}

		public void setOpponentPts(int opponentPts) {
			this.opponentPts = opponentPts;
		}

		private int rapsPts;
		private int opponentPts;
	}

}
