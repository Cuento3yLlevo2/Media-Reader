package tv.nsing.mediareader.core

interface Downloader {
    fun downloadFile(url: String): Long
}