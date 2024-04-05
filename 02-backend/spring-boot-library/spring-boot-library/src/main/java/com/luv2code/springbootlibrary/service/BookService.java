package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.CheckoutRepository;
import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.entity.Checkout;
import com.luv2code.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    //construction dependency injection
    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository){
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception{

        Optional<Book> book = bookRepository.findById(bookId);//maybe we will not find the book that's why we use optional

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);//we verify if it is already checked

        if (!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0){
            throw new Exception("Book does not exist or already checked out by user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);//after we check out, we want to extract the book checked out
        bookRepository.save(book.get());

        Checkout checkout = new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),//the loan end in 7 days
                book.get().getId()
        );

        checkoutRepository.save(checkout);

        return book.get();

    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId){
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (validateCheckout != null){
            return true;
        }else {
            return false;
        }


    }

    public int currentLoansCount(String userEmail) {

        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    /*public List<ShelfCurrentLoansResponse> currentLaons(String userEmail) throws Exception{
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);//list of books that are checked out by the user
        List<Long> bookIdList = new ArrayList<>();

        for (Checkout i: checkoutList){//we want to extract just the ids form the checkout
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);//we need all the books with these id
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books){
            Optional<Checkout> checkout = checkoutList.stream()//we are not sure if there are any real items
                    .filter(x -> x.getBookId() == book.getId()).findFirst();//after we find it we stop

            if (checkout.isPresent()){
                Date d1 = sdf.parse(checkout.get().getReturnDate());//date of the book
                Date d2 = sdf.parse(LocalDate.now().toString());//todays date

                TimeUnit time = TimeUnit.DAYS;

                long difference_IN_Time = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) difference_IN_Time));
            }
        }
        return shelfCurrentLoansResponses;
    }*/
}
