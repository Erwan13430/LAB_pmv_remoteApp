package fr.falling_knife.lab.pmv

enum class CommunicationType(string: Any?) {
    TRNSFRTRUN("transfertRunner"),
    CSV("getCsv"),
    OLD_SESSION("oldSessionExist"),
    BTN_START_SESSION("btnStartSession"),
    BTN_PREP("btnPrep"),
    AUTH_CHECK("authCheck"),
    BTN_AVM("btnAVM")
}