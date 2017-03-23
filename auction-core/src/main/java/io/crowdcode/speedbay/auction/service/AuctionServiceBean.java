package io.crowdcode.speedbay.auction.service;

import io.crowdcode.speedbay.auction.exception.AuctionExpiredException;
import io.crowdcode.speedbay.auction.exception.AuctionNotFoundException;
import io.crowdcode.speedbay.auction.exception.BidTooLowException;
import io.crowdcode.speedbay.auction.model.Auction;
import io.crowdcode.speedbay.auction.model.Bid;
import io.crowdcode.speedbay.auction.repository.AuctionRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ingo Düppe (Crowdcode)
 */
@Slf4j
@Setter
public class AuctionServiceBean implements AuctionService {

    private AuctionRepository auctionRepository;

    public AuctionServiceBean() {
    }

    public AuctionServiceBean(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public Long placeAuction(String title, String description, BigDecimal minAmount) {

        if (minAmount == null || minAmount.compareTo(BigDecimal.ONE) <= 0) {
            minAmount = BigDecimal.ONE;
        }

        Auction auction = new Auction()
                .setOwner("owner") // wo kommt der Principal her
                .setTitle(title)
                .setDescription(description)
                .setMinAmount(minAmount)
                .setBeginDate(LocalDateTime.now())
                .setExpireDate(LocalDateTime.now().plusMinutes(5));

        Auction save = auctionRepository.save(auction);
        return save.getId();
    }

    public Auction findAuction(Long auctionId) {
        return auctionRepository.findOne(auctionId);
    }

    public List<Auction> findRunningAuctions() {
        return auctionRepository
                .findAll()
                .parallelStream()
                .filter(Auction::isRunning)
                .collect(Collectors.toList());
    }

    public List<Auction> findExpiredAuctions() {
            return auctionRepository
                    .findAll()
                    .parallelStream()
                    .filter(Auction::isExpired)
                    .collect(Collectors.toList());
    }

    public void bidOnAuction(final Long auctionId, BigDecimal amount) throws AuctionNotFoundException, AuctionExpiredException, BidTooLowException {
        Auction auction = auctionRepository.findOne(auctionId);

        if (auction == null) {
            throw new AuctionNotFoundException(auctionId);
        }

        if (auction.isExpired()) {
            throw new AuctionExpiredException(auctionId);
        }

        if (auction.getMinAmount().compareTo(amount) >= 0
                || auction.getHighestBid().getAmount().compareTo(amount) >= 0) {
            throw new BidTooLowException(auction.getHighestBid());
        }

        Bid bid = new Bid().setAmount(amount).setEmail("bidder"); // Principal with his email
        auction.getBids().add(bid);
        auctionRepository.save(auction);
    }


    public void setAuctionRepository(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }
}
