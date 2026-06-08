package com.restock.platform.resource.domain.model.aggregates;

import com.restock.platform.resource.domain.model.commands.CreateCustomSupplyCommand;
import com.restock.platform.resource.domain.model.valueobjects.Price;
import com.restock.platform.resource.domain.model.valueobjects.StockRange;
import com.restock.platform.resource.domain.model.valueobjects.UnitMeasurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomSupplyUnitTest {

    private StockRange stockRangeMock;
    private Price priceMock;
    private UnitMeasurement unitMeasurementMock;

    @BeforeEach
    void setUp() {
        // Simulamos los Value Objects para aislar la prueba de la entidad
        stockRangeMock = mock(StockRange.class);
        priceMock = mock(Price.class);
        unitMeasurementMock = mock(UnitMeasurement.class);
    }

    @Test
    void givenValidCommand_whenCustomSupplyIsCreated_thenFieldsAreSetCorrectly() {
        // Arrange: Preparamos los datos falsos y el comando
        Long expectedUserId = 1L;
        Long expectedSupplyId = 100L;
        String expectedDescription = "Harina de trigo procesada";

        CreateCustomSupplyCommand command = mock(CreateCustomSupplyCommand.class);
        when(command.userId()).thenReturn(expectedUserId);
        when(command.supplyId()).thenReturn(expectedSupplyId);
        when(command.stockRange()).thenReturn(stockRangeMock);
        when(command.price()).thenReturn(priceMock);
        when(command.description()).thenReturn(expectedDescription);
        when(command.unitMeasurement()).thenReturn(unitMeasurementMock);

        // Act: Ejecutamos el constructor que queremos probar
        CustomSupply customSupply = new CustomSupply(command);

        // Assert: Verificamos que los datos se guardaron correctamente en la entidad
        assertNotNull(customSupply);
        assertEquals(expectedUserId, customSupply.getUserId());
        assertEquals(expectedSupplyId, customSupply.getSupplyId());
        assertEquals(stockRangeMock, customSupply.getStockRange());
        assertEquals(priceMock, customSupply.getPrice());
        assertEquals(expectedDescription, customSupply.getDescription());
        assertEquals(unitMeasurementMock, customSupply.getUnitMeasurement());
    }

    @Test
    void givenExistingCustomSupply_whenUpdateIsCalled_thenFieldsAreUpdated() {
        // Arrange: Creamos un CustomSupply base
        CreateCustomSupplyCommand command = mock(CreateCustomSupplyCommand.class);
        CustomSupply customSupply = new CustomSupply(command);

        // Preparamos los nuevos datos para la actualización
        Long newSupplyId = 200L;
        StockRange newStockRange = mock(StockRange.class);
        Price newPrice = mock(Price.class);
        String newDescription = "Azúcar rubia actualizada";
        UnitMeasurement newUnitMeasurement = mock(UnitMeasurement.class);

        // Act: Llamamos al método update
        CustomSupply updatedSupply = customSupply.update(
                newSupplyId, newStockRange, newPrice, newDescription, newUnitMeasurement
        );

        // Assert: Verificamos que los datos cambiaron y que retorna la misma instancia
        assertEquals(newSupplyId, updatedSupply.getSupplyId());
        assertEquals(newStockRange, updatedSupply.getStockRange());
        assertEquals(newPrice, updatedSupply.getPrice());
        assertEquals(newDescription, updatedSupply.getDescription());
        assertEquals(newUnitMeasurement, updatedSupply.getUnitMeasurement());
        assertEquals(customSupply, updatedSupply); // Verifica que retorna 'this'
    }
}