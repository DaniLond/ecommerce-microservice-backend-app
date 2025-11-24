"""
Pruebas de integración para el Shipping Service.
"""

import pytest
import uuid
from utils.api_utils import make_request, set_current_service, is_success_status


class TestShippingServiceComplete:
    """
    Pruebas completas para el Shipping Service - gestión de envíos.
    """

    def setup_method(self):
        """Configurar el servicio para las pruebas."""
        set_current_service("shipping-service")

    # ==================== PRUEBAS PARA ITEMS DE ENVÍO ====================

    @pytest.fixture
    def create_test_order_item(self):
        """Fixture para crear un item de orden de prueba."""
        # Usar IDs que no causen conflicto (productId y orderId deben ser diferentes)
        order_item_data = {
            "productId": 1,
            "orderId": 3,
            "orderedQuantity": 5,
        }

        response = make_request("POST", "/api/shippings", data=order_item_data)
        assert is_success_status(response.status_code), f"Error al crear item de orden: {response.text}"

        created_order_item = response.json()

        yield {"data": created_order_item}

        order_id = created_order_item["orderId"]
        product_id = created_order_item["productId"]
        make_request("DELETE", f"/api/shippings/{order_id}/{product_id}")

    def test_order_item_find_all(self):
        """Prueba para obtener todos los items de envío."""
        response = make_request("GET", "/api/shippings")

        assert is_success_status(response.status_code)
        result = response.json()
        assert "collection" in result
        assert isinstance(result["collection"], list)

    def test_order_item_save(self):
        """Prueba para crear un nuevo item de orden."""
        # Usar IDs altos para evitar conflictos con datos existentes
        order_item_data = {
            "productId": 1,
            "orderId": 4,
            "orderedQuantity": 10,
        }

        response = make_request("POST", "/api/shippings", data=order_item_data)

        assert is_success_status(response.status_code)
        result = response.json()
        assert result["productId"] == order_item_data["productId"]
        assert result["orderId"] == order_item_data["orderId"]
        assert result["orderedQuantity"] == order_item_data["orderedQuantity"]

        order_id = result["orderId"]
        product_id = result["productId"]
        make_request("DELETE", f"/api/shippings/{order_id}/{product_id}")

    def test_order_item_update(self, create_test_order_item):
        """Prueba para actualizar item de orden."""
        order_item = create_test_order_item
        updated_data = order_item["data"].copy()
        updated_data["orderedQuantity"] = 15

        response = make_request("PUT", "/api/shippings", data=updated_data)

        assert is_success_status(response.status_code)
        result = response.json()
        assert result["orderedQuantity"] == updated_data["orderedQuantity"]

    @pytest.mark.skip(reason="OrderId 5 no existe en la base de datos - necesita datos de prueba válidos")
    def test_order_item_quantity_management(self):
        """Prueba para gestión de cantidades de envío."""
        # Usar IDs altos para evitar conflictos
        order_item_data = {
            "productId": 1,
            "orderId": 5,
            "orderedQuantity": 20,
        }

        create_response = make_request("POST", "/api/shippings", data=order_item_data)
        assert is_success_status(create_response.status_code)
        order_item = create_response.json()

        order_item["orderedQuantity"] = 25
        update_response = make_request("PUT", "/api/shippings", data=order_item)
        assert is_success_status(update_response.status_code)
        updated_order_item = update_response.json()
        assert updated_order_item["orderedQuantity"] == 25

        order_item["orderedQuantity"] = 30
        final_update_response = make_request("PUT", "/api/shippings", data=order_item)
        assert is_success_status(final_update_response.status_code)
        final_order_item = final_update_response.json()
        assert final_order_item["orderedQuantity"] == 30

        order_id = order_item["orderId"]
        product_id = order_item["productId"]
        make_request("DELETE", f"/api/shippings/{order_id}/{product_id}")

    @pytest.mark.skip(reason="OrderId 6 no existe en la base de datos - necesita datos de prueba válidos")
    def test_order_item_with_references(self):
        """Prueba para crear item de orden con referencias a producto y orden."""
        # Usar IDs altos y sin DTOs adicionales (solo se necesitan los IDs)
        order_item_data = {
            "productId": 1,
            "orderId": 6,
            "orderedQuantity": 7,
        }

        response = make_request("POST", "/api/shippings", data=order_item_data)

        assert is_success_status(response.status_code)
        result = response.json()
        assert result["productId"] == order_item_data["productId"]
        assert result["orderId"] == order_item_data["orderId"]
        assert result["orderedQuantity"] == order_item_data["orderedQuantity"]

        if "productDto" in result and result["productDto"]:
            assert (
                result["productDto"]["productId"]
                == order_item_data["productDto"]["productId"]
            )
        if "orderDto" in result and result["orderDto"]:
            assert (
                result["orderDto"]["orderId"] == order_item_data["orderDto"]["orderId"]
            )

        order_id = result["orderId"]
        product_id = result["productId"]
        make_request("DELETE", f"/api/shippings/{order_id}/{product_id}")
