package dev.rnett.spekt

import dev.rnett.symbolexport.symbol.compiler.name
import org.jetbrains.kotlin.name.Name

object Names {
    val InSpektAnnotation = Symbols.spekt.dev_rnett_spekt_InSpekt
    val Spekt = Symbols.spekt.dev_rnett_spekt_Spekt

    object Impl {
        val getObjectInstance = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_getObjectInstance.name()
        val getCaster = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_getCaster.name()

        object Ctor {
            val kClass = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_kClass
            val isAbstract = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_isAbstract
            val packageNames = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_packageNames
            val classNames = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_classNames
            val supertypes = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_supertypes
            val annotations = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_annotations
            val functions = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_functions
            val properties = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_properties
            val constructors = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_constructors
            val sealedSubclasses = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_init_sealedSubclasses
        }

        object FunctionCtor {
            val name = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_name
            val isAbstract = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_isAbstract
            val kotlin = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_kotlin
            val annotations = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_annotations
            val parameters = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_parameters
            val returnType = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_returnType
            val isSuspend = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_isSuspend
            val isPrimaryCtor = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_isPrimaryCtor
            val invoker = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_invoker
            val suspendInvoker = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_suspendInvoker
            val inheritedFrom = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Function_init_inheritedFrom
        }

        object ParamCtor {
            val name = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Param_init_name
            val annotations = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Param_init_annotations
            val hasDefault = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Param_init_hasDefault
            val type = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Param_init_type
            val globalIndex = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Param_init_globalIndex
            val indexInKind = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Param_init_indexInKind
            val kind = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Param_init_kind
        }

        object PropertyCtor {
            val name = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_name
            val annotations = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_annotations
            val isMutable = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_isMutable
            val isInConstructor = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_isInConstructor
            val hasBackingField = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_hasBackingField
            val isAbstract = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_isAbstract
            val hasDelegate = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_hasDelegate
            val type = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_type
            val kotlin = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_kotlin
            val getter = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_getter
            val setter = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_setter
            val inheritedFrom = Symbols.spekt.dev_rnett_spekt_internal_SpektImplementationV1_Property_init_inheritedFrom
        }
    }

    object Caster {
        val cast = Symbols.spekt.dev_rnett_spekt_Caster_cast
        val castValue = Symbols.spekt.dev_rnett_spekt_Caster_cast_value
        val isInstance = Symbols.spekt.dev_rnett_spekt_Caster_isInstance
        val isInstanceValue = Symbols.spekt.dev_rnett_spekt_Caster_isInstance_value
        val safeCast = Symbols.spekt.dev_rnett_spekt_Caster_safeCast
        val safeCastValue = Symbols.spekt.dev_rnett_spekt_Caster_safeCast_value
    }


}

object GeneratedNames {
    val SpektImplV1 = Name.identifier("SpektImplV1")
    val CasterImpl = Name.identifier("CasterImpl")

    val spektMethod = Name.identifier("spekt")
}