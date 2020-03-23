package cn.yiiguxing.plugin.translate.ui

import cn.yiiguxing.plugin.translate.*
import cn.yiiguxing.plugin.translate.ui.form.SupportForm
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.labels.LinkLabel
import com.intellij.util.ui.JBUI
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.UIManager
import javax.swing.event.HyperlinkEvent

/**
 * SupportDialog
 */
class SupportDialog private constructor() : DialogWrapper(null) {

    private val form = SupportForm()

    init {
        title = message("support")
        setOKButtonText(message("support.thanks"))
        form.init()
        init()
    }

    private fun SupportForm.init() {
        rootPane.border = JBUI.Borders.empty(12, 15)
        rootPane.background = UIManager.getColor("TextArea.background")

        starLinkLabel.init(GITHUB_URL)
        prLinkLab.init(GITHUB_URL)
        reportLinkLabel.init(NEW_ISSUES_URL)
        ideaLinkLabel.init(NEW_ISSUES_URL)
        openCollectiveLinkLabel.init(OPEN_COLLECTIVE_URL, false)

        donateLinkLabel.icon = null
        donateLinkLabel.setListener({ _, _ -> showDonatePop(donateLinkLabel) }, SUPPORT_PATRONS_URL)
    }

    private fun LinkLabel<String>.init(url: String, cleanIcon: Boolean = true) {
        if (cleanIcon) {
            icon = null
        }
        setListener({ _, linkUrl -> BrowserUtil.browse(linkUrl) }, url)
    }

    private fun showDonatePop(component: JComponent) {
        @Suppress("SpellCheckingInspection")
        val content = """
            使用支付宝/微信支付捐赠时请提供名字/昵称和网站，格式为：<br/>
            <i><b>名字/昵称 [&lt;网站>][：留言]</i></b><br/>
            网站与留言为可选部分，以下是一个例子：<br/>
            <i><b>Yii.Guxing &lt;github.com/YiiGuxing>：加油！</i></b><br/>
            提供的信息将会被添加到<a href="#patrons"><b>Patrons/捐赠者</b></a>列表中，列表将按捐赠总额列出前50名捐赠者。
        """.trimIndent()
        JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder(content, null, BALLOON_FILL_COLOR) {
                if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                    BrowserUtil.browse(SUPPORT_PATRONS_URL)
                }
            }
            .setShadow(true)
            .setHideOnAction(true)
            .setHideOnClickOutside(true)
            .setHideOnFrameResize(true)
            .setHideOnKeyOutside(true)
            .setHideOnLinkClick(true)
            .setDisposable(disposable)
            .setContentInsets(JBUI.insets(10))
            .createBalloon()
            .show(JBPopupFactory.getInstance().guessBestPopupLocation(component), Balloon.Position.above)
    }

    override fun createCenterPanel(): JComponent = form.rootPane

    override fun getStyle(): DialogStyle = DialogStyle.COMPACT

    override fun createActions(): Array<Action> = arrayOf(okAction)

    companion object {
        private val BALLOON_FILL_COLOR = JBColor(0xE4E6EB, 0x45494B)

        fun show() = SupportDialog().show()
    }

}