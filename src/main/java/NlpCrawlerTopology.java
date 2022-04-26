import bolts.*;
import org.apache.storm.topology.ConfigurableTopology;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import spouts.TestDocumentSpout;

public class NlpCrawlerTopology extends ConfigurableTopology {
    public static void main(String[] args) {
        ConfigurableTopology.start(new NlpCrawlerTopology(), args);
    }
    @Override
    protected int run(String[] args) {
        //todo:make bolts for each of the opennlp processes
        String topologyName = "test";
        conf.setDebug(false);
//        if (args != null && args.length > 0) {
//            topologyName = args[0];
//        }
        /*todo:I don't need this much parrellism, do it at a higher level.
            just document -> names is fine?
            maybe document -> tokens -> names + count
            data actually needed from each document:
            names and name counts
            orgs and counts

        */
        TopologyBuilder builder = new TopologyBuilder();

        //todo:get document from stormcrawler
        builder.setSpout("document", new TestDocumentSpout(), 1);

        //todo:detect the language (only deal with english for now)
        // builder.setBolt("langdoc", new LangDetectBolt(), 1).shuffleGrouping("doc");

        //todo:categorise the document topic
        //builder.setBolt("catdoc", new DoCatBolt(), 1).shuffleGrouping("langdoc");

        //get the names, organisations, locations and dates
        builder.setBolt("entities", new EntityFinderBolt(), 1).localOrShuffleGrouping("document");

        //todo:count the mentions of names and organsations
        builder.setBolt("names",new NamePrintBolt()).shuffleGrouping("entities", "name");
        builder.setBolt("orgs",new OrgPrintBolt()).shuffleGrouping("entities","org");
        builder.setBolt("locations",new LocPrintBolt()).shuffleGrouping("entities","location");
        builder.setBolt("dates",new DatePrintBolt()).shuffleGrouping("entities","date");

        //todo:link names, organisation and locations to own db
        //builder.setBolt("links", new EntityLinkerBolt(), 1).shuffleGrouping("names");

        //todo:store url with namedentites and mention count

        //todo:record unknown names and organisations for further training.

        return submit(topologyName, conf, builder);
    }
}
