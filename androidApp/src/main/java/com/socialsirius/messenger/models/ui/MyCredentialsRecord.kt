package com.socialsirius.messenger.models.ui

import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.ProposedAttrib
import com.sirius.library.mobile.models.CredentialsRecord
import java.io.Serializable


class MyCredentialsRecord  : Serializable {
    var cred_rev_id: String? = null
    var rev_reg_id: String? = null
    var referent: String? = null
    var schema_id: String? = null
    var cred_def_id: String? = null
    var attrs: Map<String, String>? = null


    fun getAttributes(): List<ProposedAttrib> {
        return attrs?.map {
            ProposedAttrib(it.key, it.value, null)
        }.orEmpty()
    }

    companion object {
        fun map(credentialsRecord: CredentialsRecord?) : MyCredentialsRecord{
           val record =  MyCredentialsRecord()
            record.attrs = credentialsRecord?.attrs
            record.cred_def_id = credentialsRecord?.cred_def_id
            record.cred_rev_id = credentialsRecord?.cred_rev_id
            record.referent = credentialsRecord?.referent
            record.rev_reg_id = credentialsRecord?.rev_reg_id
            record.schema_id = credentialsRecord?.schema_id
            return record
        }
    }



}