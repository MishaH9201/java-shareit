package ru.practicum.shareit.booking;


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
    private BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingDtoForUpdate bookingDtoForUpdate;
    private Item item;
    public final String userId = "X-Sharer-User-Id";
    private LocalDateTime date = LocalDateTime.now();

    @BeforeEach
    void beforeEach() {
        item = new Item(1L, "Pen", "penapple", true, new User(2L, "m@MMM.w", "Jon"), null);
        booking = new Booking(
                1L, date.plusSeconds(1), date.plusHours(4),
                item, new User(1L, "q@q.com", "Tim"), BookingStatus.WAITING);
        bookingDto = new BookingDto(
                1L, date.plusDays(1), date.plusDays(2),
                1L, 1L, BookingStatus.WAITING);
        bookingDtoForUpdate = new BookingDtoForUpdate(1L, date.plusSeconds(1), date.plusHours(1), item,
                new User(1L, "q@q.com", "Tim"), BookingStatus.WAITING);
    }

    @Test
    void testAddNewBooking() throws Exception {
        when(bookingService.save(any(), anyLong())).thenReturn(booking);
        mockMvc.perform(post("/bookings")
                        .header(userId, 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService, times(1)).save(any(), anyLong());
    }

    @Test
    void testUpdateBooking() throws Exception {
        when(bookingService.update(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);
        mockMvc.perform(patch("/bookings/{bookingId}?approved={approved}", 1L, true)
                        .header(userId, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().toString())));
        verify(bookingService, times(1)).update(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void testFindBookingByIdWhenIdIsValid() throws Exception {
        when(bookingService.findBookingById(anyLong(), anyLong())).thenReturn(bookingDtoForUpdate);
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", 1L)
                        .header(userId, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoForUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDtoForUpdate.getStatus().toString())));
        verify(bookingService, times(1)).findBookingById(anyLong(), anyLong());
    }

    @Test
    void testFindAllBookingsByUserIdWhenIdIsValid() throws Exception {
        when(bookingService.findAllBookingsByUserId(anyLong(), any(), any())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings?from={from}&size={size}", 1, 10)
                        .header(userId, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).findAllBookingsByUserId(anyLong(), any(), any());
    }

    @Test
    void testFindAllBookingsForItemsUser() throws Exception {
        when(bookingService.findAllBookingsForItemsUser(anyLong(), any(), any())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner?from={from}&size={size}", 1, 10)
                        .header(userId, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).findAllBookingsForItemsUser(anyLong(), any(), any());
    }
}