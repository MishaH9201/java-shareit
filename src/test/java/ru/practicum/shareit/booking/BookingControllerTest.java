package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForUpdate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
Booking booking;
    BookingDto bookingDto;
    BookingDtoForUpdate bookingDtoForUpdate;
    Item item;
    public final String USER_ID = "X-Sharer-User-Id";

    @BeforeEach
    void beforeEach(){
        item = new Item(1l, "Pen", "penapple",true,new User(2L, "m@MMM.w","Jon"),null);
        booking = new Booking(
                1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusHours(4),
                item, new User(1l, "q@q.com", "Tim"), BookingStatus.WAITING );
        bookingDto = new BookingDto(
                1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                1L, new User(1l, "q@q.com", "Tim"), BookingStatus.WAITING );
        bookingDtoForUpdate = new BookingDtoForUpdate(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusHours(1), item,
                new User(1l, "q@q.com", "Tim"),BookingStatus.WAITING);
    }

    @Test
    void add() throws Exception {
        when(bookingService.save(any(),anyLong())).thenReturn(booking);
        mockMvc.perform(post("/bookings")
                        .header(USER_ID, 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()),Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService, times(1)).save(any(), anyLong());
    }

    @Test
    void update() throws Exception{
        when(bookingService.update(anyLong(),anyLong(),anyBoolean())).thenReturn(booking);
        mockMvc.perform(patch("/bookings/{bookingId}?approved={approved}",1L,true)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()),Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
        verify(bookingService, times(1)).update(anyLong(),anyLong(),anyBoolean());
    }

    @Test
    void findBookingById() throws Exception {
        when(bookingService.findBookingById(anyLong(), anyLong())).thenReturn(bookingDtoForUpdate);
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", 1L)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoForUpdate.getId()),Long.class))
                .andExpect(jsonPath("$.status", is(bookingDtoForUpdate.getStatus().toString())));
        verify(bookingService, times(1)).findBookingById(anyLong(), anyLong());
    }

    @Test
    void findAllBookingsByUserId() throws Exception {
        when(bookingService.findAllBookingsByUserId(anyLong(), any(),any())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings?from={from}&size={size}",1,10)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).findAllBookingsByUserId(anyLong(), any(),any());
    }

    @Test
    void findAllBookingsForItemsUser() throws Exception {
        when(bookingService.findAllBookingsForItemsUser(anyLong(), any(),any())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner?from={from}&size={size}", 1,10)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).findAllBookingsForItemsUser(anyLong(), any(),any());
    }
}