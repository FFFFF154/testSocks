package ru.alekseev.testsocks.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alekseev.testsocks.dto.SocksDto;
import ru.alekseev.testsocks.entity.Socks;
import ru.alekseev.testsocks.exceptions.InvalidFormatException;
import ru.alekseev.testsocks.repository.SocksRepository;
import ru.alekseev.testsocks.service.SocksService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @InjectMocks
    private MainController mainController;
    @Mock
    private SocksService socksService;

    @Test
    void income_ReturnsValidResponseEntity() {
        SocksDto socksDto1 = new SocksDto("red", 50.5, 100);
        SocksDto socksDto2 = new SocksDto();
        Mockito.when(socksService.addSocks(socksDto1)).thenReturn(true);
        boolean result = socksService.addSocks(socksDto1);
        assertTrue(result);
    }

    @Test
    void allSocks_ReturnsAllSocks(){
        var arrayOfSocks = List.of(new Socks(2L, "white", 50.0, 100),
                new Socks(3L, "red", 30.0, 200));
        Mockito.doReturn(arrayOfSocks).when(socksService).socksFilter(null, null, null, null, null, null);

        var result = this.mainController.allSocks(null, null, null, null, null, null);

        assertNotNull(result);
        assertEquals(arrayOfSocks, result.getBody());
    }

    @Test
    void outcome_ReturnsFalseResponse() {
        SocksDto socksDto = new SocksDto("red", 50.5, 1000);
        Mockito.when(this.socksService.removeSocks(socksDto)).thenReturn(false);

        boolean result = socksService.removeSocks(socksDto);

        assertFalse(result);
    }

    @Test
    void updateSocks_ReturnsTrueResponse() {
        Long id = 3L;
        SocksDto socksDto = new SocksDto("red", 50.5, 1000);
        Mockito.when(this.socksService.updateSocks(id, socksDto)).thenReturn(true);
        boolean result = socksService.updateSocks(id, socksDto);

        assertTrue(result);
    }
}