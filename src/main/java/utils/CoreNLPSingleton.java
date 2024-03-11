//package utils;
//
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//
//import java.util.Properties;
//
//public class CoreNLPSingleton {
//    private StanfordCoreNLP pipeline;
//
//    private CoreNLPSingleton() {
//        // set up pipeline properties
//        Properties props = new Properties();
//        // set the list of annotators to run
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
//        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
//        props.setProperty("coref.algorithm", "neural");
//        // build pipeline
//        pipeline = new StanfordCoreNLP(props);
//        System.out.println("CoreNLP process started");
//    }
//
//    public static CoreNLPSingleton getInstance() {
//        return CoreNLPHolder.instance;
//    }
//
//    private static final class CoreNLPHolder {
//        private static final CoreNLPSingleton instance = new CoreNLPSingleton();
//    }
//    public StanfordCoreNLP getPipeline() {
//        return pipeline;
//    }
//}
