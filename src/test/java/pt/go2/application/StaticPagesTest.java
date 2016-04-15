package pt.go2.application;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;

import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.Assert;
import org.junit.Test;

import pt.go2.fileio.Configuration;
import pt.go2.fileio.RestoreItem;
import pt.go2.mocks.HttpServletRequestMock;
import pt.go2.mocks.HttpServletResponseMock;
import pt.go2.response.Response;
import pt.go2.storage.KeyValueStore;

public class StaticPagesTest {

    @Test
    public void test() throws IOException, ServletException, URISyntaxException {

        final String redirected = "http://redirected.com";

        final List<RestoreItem> uris = Arrays.asList(new RestoreItem("aabbcc", redirected));

        final Configuration config = new Configuration();

        final KeyValueStore ks = new KeyValueStore(uris, config.getDbFolder());
        final EmbeddedPages res = new EmbeddedPages(config);

        final HttpServletRequestMock request = new HttpServletRequestMock() {

            @Override
            public String getRequestURI() {
                return "/aabbcc";
            }

        };

        final HttpServletResponseMock response = new HttpServletResponseMock();

        final AbstractHandler sp = new RequestHandler(config, ks, res, null);

        sp.handle("", null, request, response);

        Assert.assertEquals(config.getRedirect(), response.getStatus());
        Assert.assertEquals(redirected, response.getHeader(Response.RESPONSE_HEADER_LOCATION));

    }
}
