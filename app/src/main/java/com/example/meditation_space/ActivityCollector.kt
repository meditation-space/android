package com.example.meditation_space

import android.app.Activity

object ActivityCollector {
    private val activities = ArrayList<Activity>()
    private var activityCount = 0
    private var isCollectRainDrop = false//记录是否正在收集雨水
    private var isViolation = false//记录是否违规
    private var user_id = 1//记录用户id
    private var sunlight = 0//记录阳光
    private var raindrop = 0//记录雨滴

    fun getIsCollectRainDrop(): Boolean {
        return isCollectRainDrop
    }

    fun setIsCollectRainDrop(value: Boolean) {
        isCollectRainDrop = value
    }

    fun getSunLight(): Int {
        return sunlight
    }

    fun setSunLight(value: Int) {
        sunlight = value
    }

    fun getRainDrop(): Int {
        return raindrop
    }

    fun setRainDrop(value: Int) {
        raindrop = value
    }


    fun getUser_id(): Int {
        return user_id
    }

    fun setUser_id(value: Int) {
        user_id = value
    }

    fun getIsViolation(): Boolean {
        return isViolation
    }

    fun setIsViolation(value: Boolean) {
        isViolation = value
    }

    fun getActivityCount(): Int {
        return activityCount
    }

    fun setActivityCount(value: Int) {
        activityCount = value
    }

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun finishAll() {
        for (activity in activities) {
            if (!activity.isFinishing) {
                activity.finish()
            }
            activities.clear()
        }
    }
}