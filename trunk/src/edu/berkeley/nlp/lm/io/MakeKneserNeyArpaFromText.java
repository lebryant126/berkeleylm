package edu.berkeley.nlp.lm.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.berkeley.nlp.lm.ConfigOptions;
import edu.berkeley.nlp.lm.ContextEncodedProbBackoffLm;
import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.StringWordIndexer;
import edu.berkeley.nlp.lm.util.Logger;

/**
 * Estimates a Kneser-Ney language model from raw text, and writes the language
 * model out in ARPA-format. This is meant to closely resemble the functionality
 * of SRILM's
 * <code>ngram-count -text &lt;text file&gt; -ukndiscount -lm &lt;outputfile&gt;), 
 * with two main exceptions: 
 * <br> (a) rather than calculating the discount for each n-gram order from counts, we use a constant discount of 0.75 for all orders
 * <br> (b) Count thresholding is currently not implemented (SRILM by default thresholds counts for n-grams with n > 3). 
 * 
 * Note that if the input/output files have a .gz suffix, they will be unzipped/zipped as necessary.
 * 
 * @author adampauls
 * 
 */
public class MakeKneserNeyArpaFromText
{

	/**
	 * 
	 */
	private static void usage() {
		System.err.println("Usage: <lmOrder> <ARPA lm output file> <textfiles>+");
		System.exit(1);
	}

	public static void main(final String[] argv) {
		if (argv.length < 3) {
			usage();
		}
		int lmOrder = Integer.parseInt(argv[0]);
		String outputFile = argv[1];
		List<File> inputFiles = new ArrayList<File>();
		for (int i = 2; i < argv.length; ++i) {
			inputFiles.add(new File(argv[i]));
		}
		Logger.setGlobalLogger(new Logger.SystemLogger(System.out, System.err));
		Logger.startTrack("Reading text files " + inputFiles + " and writing to file " + outputFile);
		final StringWordIndexer wordIndexer = new StringWordIndexer();
		wordIndexer.setStartSymbol(ArpaLmReader.START_SYMBOL);
		wordIndexer.setEndSymbol(ArpaLmReader.END_SYMBOL);
		wordIndexer.setUnkSymbol(ArpaLmReader.UNK_SYMBOL);
		LmReaders.createKneserNeyLmFromTextFiles(inputFiles, wordIndexer, lmOrder, new File(outputFile), new ConfigOptions());
		Logger.endTrack();
	}

}