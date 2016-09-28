package io.crowdcode.speedbay.auction.config;

import io.crowdcode.speedbay.auction.fixture.AuctionFixture;
import io.crowdcode.speedbay.auction.model.Auction;
import io.crowdcode.speedbay.auction.service.AuctionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * @author Ingo Düppe (Crowdcode)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BusinessLogicConfiguration.class})
public class BusinessLogicConfigurationSpringTest {

    @Autowired
    private AuctionService auctionService;

    @Test
    @Repeat(100)
    @DirtiesContext
    public void testConfiguration() throws Exception {
        Auction auction = AuctionFixture.buildDefaultAuction();
        Long auctionId = auctionService.placeAuction(
                auction.getTitle(),
                auction.getDescription(),
                auction.getMinAmount());


        auctionService.bidOnAuction(auctionId, BigDecimal.valueOf(11));
        Auction found = auctionService.findAuction(auctionId);

        assertThat(found.getHighestBid().getAmount().doubleValue(), is(11.0));
        assertThat(auctionService.findRunningAuctions(), hasSize(1));

    }
}