package io.github.klahap.fraploy.util

import io.github.klahap.fraploy.model.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


open class ValueClassSerializer<T : ValueClass>(
    private val kClass: KClass<T>,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(kClass.simpleName!!, PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(value.name)
    override fun deserialize(decoder: Decoder): T = kClass.primaryConstructor!!.call(decoder.decodeString())
}
