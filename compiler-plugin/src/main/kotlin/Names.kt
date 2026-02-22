package dev.rnett.inspekt

import org.jetbrains.kotlin.name.Name

object Names {
    val InspektAnnotation = Symbols.inspekt.dev_rnett_inspekt_Inspekt
    val Inspektion = Symbols.inspekt.dev_rnett_inspekt_Inspektion

    val inspect = Symbols.inspekt.dev_rnett_inspekt_inspekt

    object Impl {
        object Ctor {
            val kClass = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_kClass
            val isAbstract = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_isAbstract
            val packageNames = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_packageNames
            val classNames = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_classNames
            val supertypes = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_supertypes
            val annotations = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_annotations
            val functions = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_functions
            val properties = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_properties
            val constructors = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_constructors
            val sealedSubclasses = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_sealedSubclasses
            val cast = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_cast
            val isInstance = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_isInstance
            val safeCast = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_safeCast
            val objectInstance = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_objectInstance
            val companionObject = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_init_companionObject
        }

        object FunctionCtor {
            val packageNames = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_packageNames
            val classNames = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_classNames
            val name = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_name
            val isAbstract = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_isAbstract
            val kotlin = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_kotlin
            val annotations = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_annotations
            val parameters = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_parameters
            val returnType = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_returnType
            val isSuspend = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_isSuspend
            val isPrimaryCtor = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_isPrimaryCtor
            val invoker = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_invoker
            val suspendInvoker = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_suspendInvoker
            val inheritedFrom = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Function_init_inheritedFrom
        }

        object ParamCtor {
            val name = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Param_init_name
            val annotations = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Param_init_annotations
            val hasDefault = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Param_init_hasDefault
            val type = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Param_init_type
            val globalIndex = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Param_init_globalIndex
            val indexInKind = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Param_init_indexInKind
            val kind = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Param_init_kind
        }

        object PropertyCtor {
            val packageNames = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_packageNames
            val classNames = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_classNames
            val name = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_name
            val annotations = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_annotations
            val isMutable = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_isMutable
            val hasBackingField = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_hasBackingField
            val isAbstract = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_isAbstract
            val hasDelegate = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_hasDelegate
            val type = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_type
            val kotlin = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_kotlin
            val getter = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_getter
            val setter = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_setter
            val inheritedFrom = Symbols.inspekt.dev_rnett_inspekt_internal_InspektionResultV1_Property_init_inheritedFrom
        }

        object ProxyHelper {
            val proxyInstance = Symbols.inspekt.dev_rnett_inspekt_internal_v1ProxyHelper_proxyInstance
            val functionName = Symbols.inspekt.dev_rnett_inspekt_internal_v1ProxyHelper_functionName
            val handler = Symbols.inspekt.dev_rnett_inspekt_internal_v1ProxyHelper_handler
            val originalMethod = Symbols.inspekt.dev_rnett_inspekt_internal_v1ProxyHelper_originalMethod
            val originalProperty = Symbols.inspekt.dev_rnett_inspekt_internal_v1ProxyHelper_originalProperty
            val args = Symbols.inspekt.dev_rnett_inspekt_internal_v1ProxyHelper_args
            val isSetter = Symbols.inspekt.dev_rnett_inspekt_internal_v1ProxyHelper_isSetter
        }
    }


}

object GeneratedNames {
    fun proxyMethodField(name: String, index: Int): Name {
        return Name.identifier("proxyMethod_${index}_${name}")
    }

    val proxyHandlerField = Name.identifier("proxyHandler")

    val inspektMethod = Name.identifier("inspekt")
    val inspektImplFieldV1 = Name.identifier("inspektImplV1")
}