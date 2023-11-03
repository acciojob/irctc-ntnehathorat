package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;


    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto)throws Exception{

        //Check for validity
        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        // Incase the there are insufficient tickets
        // throw new Exception("Less tickets are available");
        //otherwise book the ticket, calculate the price and other details
        //Save the information in corresponding DB Tables
        //Fare System : Check problem statement
        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");
        //Save the bookedTickets in the train Object
        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
        //And the end return the ticketId that has come from db

//        List<Passenger> passengers = new ArrayList<>();
//        for(int passengerId : bookTicketEntryDto.getPassengerIds()){
////            Optional<Passenger> optionalPassenger = passengerRepository.findById(passengerId);
////            if(!optionalPassenger.isPresent()){
////                throw new Exception("Invalid passenger id");
////            }
//            passengers.add(passengerRepository.findById(passengerId).get());
//
//        }
////        Optional<Passenger> optionalPassenger = passengerRepository.findById(bookTicketEntryDto.getBookingPersonId());
////        if(!optionalPassenger.isPresent()){
////            throw new Exception("Invalid Booking Passenger id");
////        }
//
//        Passenger passenger = passengerRepository.findById(bookTicketEntryDto.getBookingPersonId()).get();
////        Passenger bookedPassenger = passenger;
//
////        Optional<Train> optionalTrain = trainRepository.findById(bookTicketEntryDto.getTrainId());
////        if(!optionalTrain.isPresent()){
////            throw new Exception("Invalid Booking Passenger id");
////        }
//        Train train = trainRepository.findById(bookTicketEntryDto.getTrainId()).get();
////        Train train = optionalTrain.get();
//
//        int bookedSeats = 0;
//        List<Ticket> booked = train.getBookedTickets();
//        for(Ticket ticket : booked){
//            bookedSeats += ticket.getPassengersList().size();
//        }
//
//        if(bookedSeats+bookTicketEntryDto.getNoOfSeats()> train.getNoOfSeats()){
//            throw new Exception("Less tickets are available");
//        }
//
//        String stations[] = train.getRoute().split(",");
//
////        for(int i=0;i<stations.length-1;i++){
////            if(stations[i].equals(bookTicketEntryDto.getFromStation().toString())){
////                throw new Exception("Invalid stations");
////            }
////        }
////        for(int i=1;i<stations.length;i++){
////            if(stations[i].equals(bookTicketEntryDto.getToStation().toString())){
////                throw new Exception("Invalid stations");
////            }
////        }
//
//        int x=-1,y=-1;
//        for(int i=0;i<stations.length;i++){
//            if(bookTicketEntryDto.getFromStation().toString().equals(stations[i])){
//                x=i;
//                break;
//            }
//        }
//        for(int i=0;i<stations.length;i++){
//            if(bookTicketEntryDto.getToStation().toString().equals(stations[i])){
//                y=i;
//                break;
//            }
//        }
//        if(x==-1||y==-1||y-x<0){
//            throw new Exception("Invalid stations");
//        }
//
//        int fare = 0;
//        fare = bookTicketEntryDto.getNoOfSeats()*(y-x)*300;
//
//        Ticket ticket = new Ticket();
//        ticket.setPassengersList(passengers);
//        ticket.setTrain(train);
//        ticket.setFromStation(bookTicketEntryDto.getFromStation());
//        ticket.setToStation(bookTicketEntryDto.getToStation());
//        ticket.setTotalFare(fare);
//
//        train.getBookedTickets().add(ticket);
//        train.setNoOfSeats(train.getNoOfSeats()-bookTicketEntryDto.getNoOfSeats());
//
//        passenger.getBookedTickets().add(ticket);
//
//        trainRepository.save(train);
//        Ticket savedTicket = ticketRepository.save(ticket);
//
//       return savedTicket.getTicketId();

        Train train=trainRepository.findById(bookTicketEntryDto.getTrainId()).get();
        int bookedSeats=0;
        List<Ticket>booked=train.getBookedTickets();
        for(Ticket ticket:booked){
            bookedSeats+=ticket.getPassengersList().size();
        }

        if(bookedSeats+bookTicketEntryDto.getNoOfSeats()> train.getNoOfSeats()){
            throw new Exception("Less tickets are available");
        }

        String stations[]=train.getRoute().split(",");
        List<Passenger>passengerList=new ArrayList<>();
        List<Integer>ids=bookTicketEntryDto.getPassengerIds();
        for(int id: ids){
            passengerList.add(passengerRepository.findById(id).get());
        }
        int x=-1,y=-1;
        for(int i=0;i<stations.length;i++){
            if(bookTicketEntryDto.getFromStation().toString().equals(stations[i])){
                x=i;
                break;
            }
        }
        for(int i=0;i<stations.length;i++){
            if(bookTicketEntryDto.getToStation().toString().equals(stations[i])){
                y=i;
                break;
            }
        }
        if(x==-1||y==-1||y-x<0){
            throw new Exception("Invalid stations");
        }
        Ticket ticket=new Ticket();
        ticket.setPassengersList(passengerList);
        ticket.setFromStation(bookTicketEntryDto.getFromStation());
        ticket.setToStation(bookTicketEntryDto.getToStation());

        int fare=0;
        fare=bookTicketEntryDto.getNoOfSeats()*(y-x)*300;

        ticket.setTotalFare(fare);
        ticket.setTrain(train);

        train.getBookedTickets().add(ticket);
        train.setNoOfSeats(train.getNoOfSeats()-bookTicketEntryDto.getNoOfSeats());

        Passenger passenger=passengerRepository.findById(bookTicketEntryDto.getBookingPersonId()).get();
        passenger.getBookedTickets().add(ticket);

        trainRepository.save(train);

        return ticketRepository.save(ticket).getTicketId();

    }
}