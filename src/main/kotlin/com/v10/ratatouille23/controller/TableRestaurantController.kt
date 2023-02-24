package com.v10.ratatouille23.controller

import com.v10.ratatouille23.dto.ServerResponse
import com.v10.ratatouille23.dto.TableRestaurantDto
import com.v10.ratatouille23.dto.request.DishRequestDto
import com.v10.ratatouille23.dto.request.TableRequestDto
import com.v10.ratatouille23.mapper.DishMapper
import com.v10.ratatouille23.mapper.MenuMapper
import com.v10.ratatouille23.mapper.TableRestaurantMapper
import com.v10.ratatouille23.service.MenuService
import com.v10.ratatouille23.service.TableRestaurantService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tablerestaurant")
class TableRestaurantController(
    private val tableRestaurantMapper: TableRestaurantMapper,
    private val tableRestaurantService: TableRestaurantService

    ){

    @PostMapping("add")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR')")
    fun add(@RequestBody tableRequestDto: TableRequestDto) =
        ResponseEntity.ok(ServerResponse.ok(this.tableRestaurantMapper.toDomain(tableRestaurantService.add(tableRequestDto))))

}