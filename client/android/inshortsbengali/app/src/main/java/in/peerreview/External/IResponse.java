package in.peerreview.External;

import org.json.JSONObject;

/**
 * Created by ddutta on 8/22/2017.
 */
public interface IResponse {
    void success(JSONObject jsonObject);
    void error(String msg);
}
