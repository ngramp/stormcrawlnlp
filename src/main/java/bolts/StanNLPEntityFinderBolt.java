//package bolts;
//
//import edu.stanford.nlp.ling.CoreLabel;
//import edu.stanford.nlp.pipeline.CoreDocument;
//import edu.stanford.nlp.pipeline.CoreSentence;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import org.apache.storm.task.OutputCollector;
//import org.apache.storm.task.TopologyContext;
//import org.apache.storm.topology.OutputFieldsDeclarer;
//import org.apache.storm.topology.base.BaseRichBolt;
//import org.apache.storm.tuple.Tuple;
//import utils.CoreNLPSingleton;
//
//import java.util.List;
//import java.util.Map;
//
//public class StanNLPEntityFinderBolt extends BaseRichBolt {
//    OutputCollector collector;
//    StanfordCoreNLP pipeline;
//    @Override
//    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
//        this.collector = collector;
//        this.pipeline = CoreNLPSingleton.getInstance().getPipeline();
//    }
//
//    @Override
//    public void execute(Tuple input) {
//
//        //todo: might still have to use openNLP for document classification
//
//        // create a document object
//        CoreDocument document = new CoreDocument(input.getString(0));
//        // annnotate the document
//        pipeline.annotate(document);
//        // examples
//
//        // text of the first sentence
//        String sentenceText = document.sentences().get(0).text();
//        System.out.println("Example: first sentence");
//        System.out.println(sentenceText);
//        System.out.println();
//        // 10th token of the document
//        CoreLabel token = document.tokens().get(9);
//        System.out.println("Example: token");
//        System.out.println(token);
//        System.out.println();
//
//
//        // second sentence
//        CoreSentence sentence = document.sentences().get(1);
//        System.out.println("Example: second sentence");
//        System.out.println(sentence.text());
//        // list of the ner tags for the second sentence
//        List<String> nerTags = sentence.nerTags();
//        System.out.println("Example: ner tags");
//        System.out.println(nerTags);
//        System.out.println(sentence.entityMentions());
//        collector.ack(input);
//    }
//
//    @Override
//    public void declareOutputFields(OutputFieldsDeclarer declarer) {
//
//    }
//}
