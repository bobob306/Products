package com.benshapiro.products.utilities

// very simple interface to go from downloaded entity to local model or vice versa
interface ModelMapper<Entity, Model> {

    fun toModel(entity: Entity): Model

    fun fromModel(model: Model): Entity

}