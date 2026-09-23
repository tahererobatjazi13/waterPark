package ir.kitgroup.partnerManagement.core.ui.util.extensions

import ir.kitgroup.partnerManagement.core.ui.util.Status

fun Status.toDisplayName(): String = when (this) {
    Status.PLANNED -> "برنامه‌ریزی شده"
    Status.DONE -> "انجام شده"
    Status.CANCELLED -> "لغو شده"
    else -> "نامشخص"
}



/*
git add .
git commit -m "redesign models in screens"
git push -u origin master
git push
*/

/*
http://178.131.164.145:52439/WebService.asmx
*/
