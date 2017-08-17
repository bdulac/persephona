package io.github.bdulac.persephona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import edu.stanford.nlp.coref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.io.ReaderInputStream;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * <em>Persephona</em> is a very simple text parser.
 * <p>
 * It encapsulates the use of the <em>Stanford NLP</em> tools.
 * </p>
 */
public class Persephona {
	
	private static Logger logger = Logger.getLogger(Persephona.class.getName());
	
	public void parse(CharSequence sequence) throws IOException {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		loadLanguage(props, "french");
		loadLanguage(props, "german");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(sequence.toString());
		// run all Annotators on this text
		pipeline.annotate(document);
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the tokenString pos =
				// token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
				logger.info(word + " (" + token.tag() + "), entity=" + ne);
			}
			// TODO test the parse tree of the current sentence
			// Tree tree = sentence.get(TreeAnnotation.class);
			// TODO test the Stanford dependency graph of the current sentence
			// SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		}

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
		if (graph != null) {
			for (Integer index : graph.keySet()) {
				CorefChain chain = graph.get(index);
				logger.info("Relationship " + index + ": " + chain.getRepresentativeMention());
			}
		}

	}

	/**
	 * Adds a language model file to a pipeline properties.
	 * @param props
	 * The pipeline properties to fill.
	 * @param langSuffix
	 * The language file name suffix (e.g. 'french', 'german').
	 */
	public void loadLanguage(Properties props, String langSuffix) throws IOException {
		// InputStream inStream =
		// getClass().getResourceAsStream("StanfordCoreNLP-french.properties");
		BufferedReader reader = 
				IOUtils.readerFromString("StanfordCoreNLP-" + langSuffix + ".properties");
		InputStream inStream = new ReaderInputStream(reader);
		props.load(inStream);
	}
}