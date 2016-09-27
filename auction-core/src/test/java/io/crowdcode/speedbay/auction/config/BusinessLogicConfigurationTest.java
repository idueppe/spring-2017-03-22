package io.crowdcode.speedbay.auction.config;

import io.crowdcode.speedbay.auction.fixture.AuctionFixture;
import io.crowdcode.speedbay.auction.model.Auction;
import io.crowdcode.speedbay.auction.service.AuctionService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

/**
 * @author Ingo Düppe (Crowdcode)
 */
public class BusinessLogicConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() throws Exception {
        context = new AnnotationConfigApplicationContext(BusinessLogicConfiguration.class);
    }

    @Test
    public void testApplicationContextWithIntegration() throws Exception {
        AuctionService service = context.getBean("auctionService", AuctionService.class);

        Auction auction = AuctionFixture.buildDefaultAuction();

        Long auctionId = service.placeAuction(
                auction.getTitle(),
                auction.getDescription(),
                auction.getMinAmount());

        assertNotNull(auctionId);

        service.bidOnAuction(auctionId, BigDecimal.valueOf(11));
        Auction found = service.findAuction(auctionId);
        assertThat(found.getHighestBid().getAmount().doubleValue(), is(11.0));
    }
}