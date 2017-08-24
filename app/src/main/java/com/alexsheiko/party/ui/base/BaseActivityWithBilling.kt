package com.alexsheiko.party.ui.base

import android.os.Bundle
import com.alexsheiko.party.util.billing.IabHelper

abstract class BaseActivityWithBilling : BaseActivity() {

    val base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzUjiAB+nRn8fk7n4OpzJZmNjv3Icu4E/UaTg+cjSadEJo5eTw6Zs2dK1urq3AVCPEMHU+RY/shTbkOGick1nzzp0rBjBow0l1y4R7o3O0m7k8Nzmoc1sOItxahLFZgegf31YY7aS9nQvXB7BtsM8jkpeHOkxV5AFZeLdTeXV1e8yWbbM//UIivzkwg1+e5FyqGQmqiTAnNjg3IRIE25nqyN4ljt4DBdI3/9s4WLAnB9JROeB1zzl7D4BWPfjVi7M9SOe7gw463H+hKTDVbN2/W9JNBqtzO8Mm1qI2iutFgnxXOYlazZTpJUxiJbcSpzEkdkxdnHQnYdFrjBBpEnexQIDAQAB"
    lateinit var mHelper: IabHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHelper = IabHelper(this, base64EncodedPublicKey)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHelper.dispose()
    }
}