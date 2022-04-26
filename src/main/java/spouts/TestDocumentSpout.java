package spouts;

import org.apache.storm.lambda.SerializableSupplier;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StopWatch;

import java.io.*;
import java.util.Map;

public class TestDocumentSpout extends BaseRichSpout {
    SpoutOutputCollector collector;
    private static final Logger LOG = LoggerFactory.getLogger(TestDocumentSpout.class);

    @Override
    public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        //StopWatch time = new StopWatch();
        Utils.sleep(1000);
        String document = getArticle();
        LOG.debug("Emitting tuple: {}", document);
        //LOG.info("spout time:" + time.getElapsedTime());
        collector.emit(new Values(document));
    }

    @Override
    public void ack(Object msgId) {
    }

    @Override
    public void fail(Object msgId) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("document"));
    }

    protected String getArticle(){
        String articleText = null;
        try (FileInputStream inputFile = new FileInputStream("article.txt")) {
            //articleText = String.valueOf(inputFile);
            articleText = getFileContent(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articleText;
    }

    protected String getFileContent(FileInputStream fis) throws IOException
    {
        try( BufferedReader br = new BufferedReader( new InputStreamReader(fis)))
        {
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
            return sb.toString();
        }
    }

}
