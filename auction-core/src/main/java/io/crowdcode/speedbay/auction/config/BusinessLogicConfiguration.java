package io.crowdcode.speedbay.auction.config;

import io.crowdcode.speedbay.auction.model.Auction;
import io.crowdcode.speedbay.auction.model.Bid;
import io.crowdcode.speedbay.auction.repository.AuctionRepository;
import io.crowdcode.speedbay.auction.repository.inmemory.AuctionRepositoryInMemoryBean;
import io.crowdcode.speedbay.auction.service.AuctionService;
import io.crowdcode.speedbay.auction.service.AuctionServiceBean;
import io.crowdcode.speedbay.common.inmemory.InMemoryStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @author Ingo Düppe (Crowdcode)
 */
@Configuration
public class BusinessLogicConfiguration {

    @Bean
    public AuctionService auctionService(AuctionRepository auctionRepository) {
        AuctionServiceBean service = new AuctionServiceBean();
        service.setAuctionRepository(auctionRepository);
        return service;
    }

    @Bean
    public AuctionService auctionMultiService(List<AuctionRepository> auctionRepositoryList) {
        return new AuctionServiceBean();
    }

    @Bean
    @Order(1)
    public AuctionRepositoryInMemoryBean auctionRepositoryInMemoryBean(
            @Qualifier("auctionStore") InMemoryStore<?> storeAuction) {
        AuctionRepositoryInMemoryBean repository = new AuctionRepositoryInMemoryBean();
        repository.setStore((InMemoryStore<Auction>) storeAuction);
        return repository;
    }

    @Bean
    @Primary
    @Order(2)
    public AuctionRepositoryInMemoryBean aR(InMemoryStore<Auction> auctionStore) {
        AuctionRepositoryInMemoryBean auctionRepositoryInMemoryBean = new AuctionRepositoryInMemoryBean();
        auctionRepositoryInMemoryBean.setStore(auctionStore);
        return auctionRepositoryInMemoryBean;
    }

    @Bean(name = {"storeAuction", "auctionStore", "inMemoryStoreAuction"})
    public InMemoryStore<Auction> storeAuction() {
        InMemoryStore<Auction> store = new InMemoryStore<>();
        store.init();
        return store;
    }


    @Bean(initMethod = "init")
    public InMemoryStore<Bid> storeBid() {
        InMemoryStore<Bid> store = new InMemoryStore<>();
//        store.init();
        return store;
    }

}
