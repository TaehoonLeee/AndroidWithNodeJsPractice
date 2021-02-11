package com.example.nodejs.Repository

import com.example.nodejs.Model.Users
import com.example.nodejs.Network.NodeService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NodeRepository @Inject constructor(
    private val nodeService: NodeService
){
    fun getData() : Single<Users> {
        return nodeService.getData()
            .subscribeOn(Schedulers.io())
    }
}