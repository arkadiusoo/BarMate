package pl.barmate.cocktails.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HuggingFaceTranslationRequest {
    private String inputs;
    private Parameters parameters;

    public HuggingFaceTranslationRequest(String inputs, String srcLang, String tgtLang) {
        this.inputs = inputs;
        this.parameters = new Parameters(srcLang, tgtLang);
    }

    @Data
    public static class Parameters {
        @JsonProperty("src_lang")
        private String srcLang;
        @JsonProperty("tgt_lang")
        private String tgtLang;

        public Parameters(String srcLang, String tgtLang) {
            this.srcLang = srcLang;
            this.tgtLang = tgtLang;
        }
    }
}
