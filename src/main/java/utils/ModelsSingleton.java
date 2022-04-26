package utils;

import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ModelsSingleton {
    private LanguageDetectorModel langModel;
    private SentenceModel sentenceModel;
    private TokenizerModel tokenModel;
    private TokenNameFinderModel orgModel;
    private TokenNameFinderModel nameModel;
    private TokenNameFinderModel dateModel;
    private TokenNameFinderModel locModel;

    private ModelsSingleton() {
        //todo:get these files from a single server to easily train/update
        try {
            //assume we have already done lang detection
            InputStream langModIn = new FileInputStream("langdetect-183.bin");
            langModel = new LanguageDetectorModel(langModIn);
            //InputStream sentModIn = new FileInputStream("opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin");
            InputStream sentModIn = new FileInputStream("en-sent.bin");
            sentenceModel = new SentenceModel(sentModIn);
            //InputStream tokModIn = new FileInputStream("opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin");
            InputStream tokModIn = new FileInputStream("en-token.bin");
            tokenModel = new TokenizerModel(tokModIn);
            InputStream orgModIn = new FileInputStream("en-ner-organization.bin");
            orgModel = new TokenNameFinderModel(orgModIn);
            InputStream nameModIn = new FileInputStream("en-ner-person.bin");
            nameModel = new TokenNameFinderModel(nameModIn);
            InputStream dateModIn = new FileInputStream("en-ner-date.bin");
            dateModel = new TokenNameFinderModel(dateModIn);
            InputStream locModIn = new FileInputStream("en-ner-location.bin");
            locModel = new TokenNameFinderModel(locModIn);
        } catch (FileNotFoundException e) {
            System.out.println("FilenotFound error while getting models files");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("General IO error while getting models files");
            e.printStackTrace();
        }
    }

    public static ModelsSingleton getInstance() {
        return ModelsHolder.models;
    }

    public LanguageDetectorModel getLangModel() {
        return langModel;
    }

    public SentenceModel getSentenceModel() {
        return sentenceModel;
    }

    public TokenizerModel getTokenModel() {
        return tokenModel;
    }

    public TokenNameFinderModel getOrgModel() {
        return orgModel;
    }

    public TokenNameFinderModel getNameModel() {
        return nameModel;
    }

    public TokenNameFinderModel getDateModel() {
        return dateModel;
    }

    public TokenNameFinderModel getLocModel() {
        return locModel;
    }

    private static final class ModelsHolder {
        private static final ModelsSingleton models = new ModelsSingleton();
    }
}
