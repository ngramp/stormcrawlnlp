package bolts;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spouts.TestDocumentSpout;
import utils.ModelsSingleton;
import utils.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class EntityFinderBolt extends BaseRichBolt {
    OutputCollector collector;
    private TokenizerModel tokenModel;
    private SentenceModel sentenceModel;
    private TokenNameFinderModel orgModel;
    private TokenNameFinderModel nameModel;
    private TokenNameFinderModel dateModel;
    private TokenNameFinderModel locModel;
    private static final Logger LOG = LoggerFactory.getLogger(EntityFinderBolt.class);

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        ModelsSingleton models = ModelsSingleton.getInstance();
        sentenceModel = models.getSentenceModel();
        tokenModel = models.getTokenModel();
        orgModel = models.getOrgModel();
        nameModel = models.getNameModel();
        dateModel = models.getDateModel();
        locModel = models.getLocModel();
    }

    @Override
    public void execute(Tuple input) {
        //StopWatch time = new StopWatch();
        String document = input.getString(0);
        //get sentences
        SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
        String[] sentences = sentenceDetector.sentDetect(document);
        //get tokens
        Tokenizer tokenizer = new TokenizerME(tokenModel);
        ArrayList<String[]> tokenizedSentence = new ArrayList<>();
        for (String sentence : sentences){
            tokenizedSentence.add(tokenizer.tokenize(sentence));
        }
        //get entities
        TokenNameFinder orgFinder = new NameFinderME(orgModel);
        TokenNameFinder nameFinder = new NameFinderME(nameModel);
        TokenNameFinder dateFinder = new NameFinderME(dateModel);
        TokenNameFinder locFinder = new NameFinderME(locModel);
        //todo:bit spammy
        //calculate distance? for this you need similar
        //count 'similar'?
        for(String[] tokens: tokenizedSentence)
        {
            for (Span org: orgFinder.find(tokens))
                collector.emit("org", new Values(org));
            for (Span name: nameFinder.find(tokens))
                collector.emit("name", new Values(name));
            for (Span date: dateFinder.find(tokens))
                collector.emit("date", new Values(date));
            for (Span location: locFinder.find(tokens))
                collector.emit("location", new Values(location));
        }
        orgFinder.clearAdaptiveData();
        nameFinder.clearAdaptiveData();
        locFinder.clearAdaptiveData();
        dateFinder.clearAdaptiveData();
        //LOG.info("EntityFinderBolt time: " + time.getElapsedTime());
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("org",new Fields("org"));
        declarer.declareStream("name",new Fields("name"));
        declarer.declareStream("location",new Fields("location"));
        declarer.declareStream("date",new Fields("date"));
    }
}
