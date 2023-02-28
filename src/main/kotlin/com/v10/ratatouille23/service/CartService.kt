package com.v10.ratatouille23.service

import com.v10.ratatouille23.component.AuthenticatedUserHelper
import com.v10.ratatouille23.dto.request.CartRequestDto
import com.v10.ratatouille23.model.Cart
import com.v10.ratatouille23.model.CartDish
import com.v10.ratatouille23.repository.CartDishRepository
import com.v10.ratatouille23.repository.CartRepository
import com.v10.ratatouille23.repository.TableRestaurantRepository
import com.v10.ratatouille23.utils.CartStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CartService(
    private val tableRestaurantRepository: TableRestaurantRepository,
    private val cartDishRepository: CartDishRepository,
    private val cartRepository: CartRepository


    ){

    @Transactional
    fun add(id: Long, cartRequestDto: CartRequestDto): Cart {
        val user = AuthenticatedUserHelper.get() ?: throw IllegalStateException("User not valid")
        val tableId = this.tableRestaurantRepository.getReferenceById(id).id ?: throw IllegalStateException("Table not found")
        val found = this.cartRepository.findByTableId(tableId)

        if (found != null && found.status == CartStatus.OPEN) {
            if (cartRequestDto.dishes != null) {
                val toSaveDishes = cartRequestDto.dishes.map {
                    CartDish(
                        id = null,
                        orderId = found.id!!,
                        it,
                        userId = user.id!!,
                        createdAt = LocalDateTime.now(),
                        updateAt = LocalDateTime.now()
                    )
                }
                this.cartDishRepository.saveAll(toSaveDishes)
            }

        } else if (found != null && found.status == CartStatus.CLOSED) {
            throw IllegalStateException("Cart already closed")

        } else if (found == null) {
            val toSave = Cart(
                id = null,
                tableId = tableId,
                status = CartStatus.OPEN,
                createdAt = LocalDateTime.now(),
                updateAt = null //TODO da controllare
            )
            val savedOrder = this.cartRepository.save(toSave)

            if (cartRequestDto.dishes != null) {
                val toSaveDishes = cartRequestDto.dishes.map {
                    CartDish(
                        id = null,
                        orderId = savedOrder.id!!,
                        it,
                        userId = user.id!!,
                        createdAt = LocalDateTime.now(),
                        updateAt = LocalDateTime.now()
                    )
                }
                this.cartDishRepository.saveAll(toSaveDishes)
            }
            return savedOrder
        }



        return found
    }


    fun closeCart(id: Long): Cart{
        val user = AuthenticatedUserHelper.get() ?: throw IllegalStateException("User not valid")
        val found = this.cartRepository.getReferenceById(id)
        val toSave = Cart(
            id = found.id,
            tableId = found.tableId,
            status = CartStatus.CLOSED,
            createdAt = found.createdAt,
            updateAt = LocalDateTime.now()
        )
        val saved = this.cartRepository.save(toSave)
        return saved
    }
}