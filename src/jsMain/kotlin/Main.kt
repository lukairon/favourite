import org.w3c.dom.Element
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLImageElement
import kotlinx.browser.document
import kotlinx.serialization.Serializable
import kotlinx.browser.window

@JsExport
@Serializable

data class Message(val topic: String, val content: String)

/*************************************************************************/
// Sprite

val DEFAULT_FRAME_RATE = 300

data class SpriteAnimation(
    val frames: List<String>,
    val frameDuration: Int = 200
) {
    val normalizedFrames: List<String> by lazy {
        val width = frames
            .flatMap { it.lines() }
            .maxOf { it.length }

        frames.map { frame ->
            frame.lines().joinToString("\n") { line ->
                line.padEnd(width)
            }
        }
    }
}

class Sprite(
    private val element: HTMLElement,
    private val animations: Map<String, SpriteAnimation>
) {
    private var animationName: String? = null
    private var frame = 0

    private var frameTimer: Int? = null
    private var cycleTimer: Int? = null

    fun play(name: String) {
        cycleTimer?.let { window.clearInterval(it) }
        cycleTimer = null

        playAnimation(name)
    }

    fun play(names: List<String>, duration: Int = 2400) {
        if (names.isEmpty()) return

        cycleTimer?.let { window.clearInterval(it) }

        var animationIndex = 0

        fun playNext() {
            playAnimation(names[animationIndex])
            animationIndex = (animationIndex + 1) % names.size
        }

        playNext()

        cycleTimer = window.setInterval({
            playNext()
        }, duration)
    }

    private fun playAnimation(name: String) {
        val animation = animations[name] ?: return

        frameTimer?.let { window.clearInterval(it) }

        animationName = name
        frame = 0

        element.textContent = animation.frames[frame]

        frameTimer = window.setInterval({
            val currentAnimation = animations[animationName] ?: return@setInterval

            frame = (frame + 1) % currentAnimation.frames.size
            element.textContent = currentAnimation.frames[frame]
        }, animation.frameDuration)
    }

    fun stop() {
        frameTimer?.let { window.clearInterval(it) }
        cycleTimer?.let { window.clearInterval(it) }

        frameTimer = null
        cycleTimer = null
        animationName = null
    }

    fun element(): HTMLElement = element
}

fun createSprite(
    parent: Element,
    animations: Map<String, SpriteAnimation>
): Sprite {
    val element = document.createElement("pre") as HTMLElement

    element.style.display = "inline-block"
    element.style.margin = "0"
    element.style.padding = "0"
    element.style.verticalAlign = "middle"
    element.style.fontFamily = "monospace"
    element.style.fontSize = "12px"
    element.style.lineHeight = "1"

    parent.appendChild(element)

    return Sprite(element, animations)
}

val samAnimations = mapOf(
    "pointingUp" to SpriteAnimation(
        frames = listOf(
            """
               _ _
              ('_')
            👆(___(☝️
               | |
            """.trimIndent(),
            """
                 _ _
             👆('_')☝️
               )___)
                | |
            """.trimIndent()
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
    "pointingLeft" to SpriteAnimation(
        frames = listOf(
            """
                _ _
               ('_')
            👈(___(👆
               /  |
            """.trimIndent(),
            """
                _ _
               ('_')
            👆(___(👈
               /  |
            """.trimIndent()
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
    "jumping" to SpriteAnimation(
        frames = listOf(
            """
              _ _
             ('_')
            /)___)\
              < >
            """.trimIndent(),
            """
              _ _
            \('_')/
             (___(
              / \
            """.trimIndent(),
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
    "posing" to SpriteAnimation(
        frames = listOf(
            """
              _ _
             ('_')/
             <)___)
              /  |
            """.trimIndent(),
            """
              _ _
            \('_')
            (___(>
             |  \
            """.trimIndent(),
        ),
        frameDuration = 500
    ),
    "browRaise" to SpriteAnimation(
        frames = listOf(
            """
              ~ _
             ('_')
            <)___)>
              /  |
            """.trimIndent(),
            """
              _ _
             ('_')
            <)___)>
              /  |
            """.trimIndent(),
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
    "headShake" to SpriteAnimation(
        frames = listOf(
            """
               _ _
             |-_-)
            <(___(>
              |  |
            """.trimIndent(),
            """
              _ _
             (-_-|
            <(___(>
              |  |
            """.trimIndent(),
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
    "angry" to SpriteAnimation(
        frames = listOf(
            """
              \ /
             ('_')
            <)___)>
              <  |
            """.trimIndent(),
            """
              \ /
             ('_')💨
            <)___)>
              /  |
            """.trimIndent(),
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
    "accusatory" to SpriteAnimation(
        frames = listOf(
            """
              \ /
             ('_')
            <)___)🫵
              <  |
            """.trimIndent(),
            """
              \ /
             ('_')💨
            <)___)🫵
              /  |
            """.trimIndent(),
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
    "hand" to SpriteAnimation(
        frames = listOf(
            """
              _ _
             ('_')
            <)___)🫴
              /  |
            """.trimIndent(),
            """
              _ _
             ('_')
            <(___(🫴
              |  \
            """.trimIndent(),
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
        "pointingUpC" to SpriteAnimation(
        frames = listOf(
            """
              _ _
              ('_')
            👆(___(☝️
             | |
            """.trimIndent(),
            """
              _ _
             👆('_')☝️
               )___)
              | |
            """.trimIndent()
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
    "jumpingC" to SpriteAnimation(
        frames = listOf(
            """
             _ _
             ('_')
            /)___)\
             < >
            """.trimIndent(),
            """
             _ _
            \('_')/
             )___)
             / \
            """.trimIndent(),
        ),
        frameDuration = DEFAULT_FRAME_RATE
    ),
    "posingC" to SpriteAnimation(
        frames = listOf(
            """
             _ _
             ('_')/
             <)___)
              /  |
            """.trimIndent(),
            """
            _ _
            \('_')
            (___(>
             |  \
            """.trimIndent(),
        ),
        frameDuration = 500
    ),
)

/*************************************************************************/
// Game

enum class Stage {
    PASSWORD,
    HOLD_SAM,
    NEXT
}

class Game {
    private val root = document.getElementById("game") ?: error("Missing game")

    private lateinit var sam: Sprite

    private var numWrong = 0

    fun start() {
        showPasswordStage()
    }

    private fun clear() {
        root.innerHTML = ""
    }

    private fun showPasswordStage() {
        clear()

        val header = document.createElement("h3")
        header.textContent = "Your journey begins here!"
        
        val dialogue = document.createElement("p")
        dialogue.textContent = "To verify your identity, enter the place we always ate at before basketball..."

        val input = document.createElement("input") as HTMLInputElement
        input.id = "password"

        val button = document.createElement("button")
        button.id = "submit"
        button.textContent = "Submit"

        root.appendChild(header)
        root.appendChild(dialogue)
        root.appendChild(input)
        root.appendChild(button)

        sam = createSprite(root, samAnimations)
        sam.play(listOf("pointingLeft"))

        button.addEventListener("click") {
            val password = input.value.lowercase()

            if (encode(password) in listOf("a2Zj", "a2VudHVja3kgZnJpZWQgY2hpY2tlbg==")) {
                showMatchingGameStage()
            } else {
                handleWrongPassword(dialogue)
            }
        }
    }

    private fun showMatchingGameStage() {
        clear()

        val images = listOf(
            "images/20250129_185813.jpg",
            "images/20250213_174253.jpg",
            "images/20250305_005834.jpg",
            "images/20250328_152224.jpg",
            "images/IMG_20220902_163349.jpg",
            "images/IMG_20230222_121138.jpg",
            "images/IMG_20230414_234242_841.jpg",
            "images/IMG_20230517_145658.jpg",
            "images/IMG_20230810_120513_280.jpg",
            "images/IMG_20230810_120526_745.jpg",
            "images/IMG_20230821_203336_977.jpg",
            "images/IMG_20230821_203411_900.jpg",
            "images/IMG_20230821_203434_077.jpg",
            "images/IMG_20230827_142939_716.jpg",
            "images/IMG_20231201_154429.jpg",
            "images/IMG_20240926_155912.jpg",
            "images/IMG-20230101-WA0000.jpg",
            "images/IMG-20230101-WA0001.jpg",
            "images/IMG-20230101-WA0002.jpg",
            "images/IMG-20230101-WA0003.jpg",
            "images/IMG-20230101-WA0006.jpg",
            "images/IMG-20230101-WA0007.jpg",
            "images/IMG-20230101-WA0010.jpg",
            "images/IMG-20230101-WA0019.jpg",
            "images/IMG-20230101-WA0020.jpg",
            "images/IMG-20230101-WA0021.jpg",
            "images/mmexport1695652252979.jpg"
        )

        val matchingGame = MatchingCardsGame(
            parent = root as HTMLElement,
            imagePaths = images,
            onComplete = {
                addDialogue("You matched them all! :)")
                showHoldSamStage()
            }
        )

        matchingGame.start()

        val container = document.createElement("div") as HTMLElement
        container.className = "stage-container"
        root.appendChild(container)

        val dialogue = document.createElement("p")
        dialogue.textContent = "Now, match pairs of pictures to prove yourself! I tried to cut down on the number of pictures so it wouldn't be as hard, but I just couldn't. So if you give up, just press this button."
        container.appendChild(dialogue)

        val buttons = document.createElement("div") as HTMLElement
        buttons.className = "button-row"
        container.appendChild(buttons)

        val revealButton = document.createElement("button") as HTMLElement
        revealButton.textContent = "I give up"
        revealButton.className = "game-button"
        buttons.appendChild(revealButton)

        val nextButton = document.createElement("button") as HTMLElement
        nextButton.textContent = "Continue"
        nextButton.className = "game-button"
        nextButton.style.display = "none"
        buttons.appendChild(nextButton)

        val sprites = document.createElement("div") as HTMLElement
        sprites.className = "sprite-row"
        container.appendChild(sprites)

        val sam = createSprite(sprites, samAnimations)
        sam.play(listOf("posingC"))

        val sam2 = createSprite(sprites, samAnimations)
        sam2.play(listOf("pointingUpC"))

        val sam3 = createSprite(sprites, samAnimations)
        sam3.play(listOf("jumpingC"))

        revealButton.addEventListener("click") {
            matchingGame.revealAll()
            revealButton.style.display = "none"
            nextButton.style.display = "inline-block"
        }

        nextButton.addEventListener("click") {
            showHoldSamStage()
        }
    }

    private fun handleWrongPassword(dialogue: Element) {
        numWrong++

        val message = document.createElement("p")
        when (numWrong) {
            1 -> {
                message.textContent = "I'll assume you just can't spell. Surely you would never forget..."
                sam.play(listOf("browRaise"))
            }
            2 -> {
                message.textContent = "Okay, I'll give you a hint: ☝️👅👍"
                sam.play(listOf("headShake"))
            }
            3 -> {
                message.textContent = "I don't think you're the birthday girl. You have one more try before I kick you out."
                sam.play(listOf("angry"))
            }
            4 -> {
                message.textContent = "🤚🛑🚫"
                sam.play(listOf("accusatory"))
                document.getElementById("password")?.classList?.add("hidden")
                document.getElementById("submit")?.classList?.add("hidden")
            }
        }
        dialogue.appendChild(message)
    }

    private fun showHoldSamStage() {
        clear()

        val dialogue = document.createElement("p")
        dialogue.textContent = decode("SEkgTEFWQU5ZQSEgU2luY2UgSSBjYW4ndCBnaXZlIHRoZSBjYXJkIHRvIHlvdSBpbiBwZXJzb24sIGhvbGQgbXkgdmlydHVhbCBoYW5kIChvciBhbnl3aGVyZSBvbiBteSBib2R5IPCfmI8pIGFuZCBJJ2xsIHRha2UgeW91IHRoZXJlLg==")
        root.appendChild(dialogue)

        sam = createSprite(root, samAnimations)
        sam.play(listOf("hand"))
        sam.element().style.cursor = "pointer"
        sam.element().addEventListener("click") {
            showNextStage()
        }
    }

    private fun showNextStage() {
        clear()

        val dialogue = document.getElementById("dialogue") ?: return
        dialogue.classList?.add("visible")
        
        addDialogue(decode("SGFwcHkgYmlydGhkYXkgTGF2YW55YSA6KQoKSSBjYW4ndCBiZWxpZXZlIHlvdSdyZSAyMCBub3csIHlvdSBjb3VsZCBiYXNpY2FsbHkgYmUgbXkgZ3JhbmRtYSDinaTvuI8KCkkndmUgYmVlbiBzbyBoYXBweSBzZWVpbmcgaG93IHRoaW5ncyBoYXZlIHR1cm5lZCBvdXQgZm9yIHlvdSB0aGlzIHBhc3QgeWVhci4gWW91J3ZlIGZvdW5kIGxvb292ZSBhbmQgYSBjaXJjbGUgb2YgcGVvcGxlIHRoYXQgc2VlbSBhbWF6aW5nLiBJIGFtIHNvIGdsYWQgeW91IGZpbmFsbHkgaGF2ZSB0aGUgc3VwcG9ydCB5b3UgZGVzZXJ2ZSAodGhvdWdoIEkgdGhpbmsgaXQncyBiZWVuIGEgbG9uZyB0aW1lIGNvbWluZywgYW5kIHlvdSBkZXNlcnZlIGV2ZW4gbW9yZSDwn6uwKS4gRXZlbiB3aXRoIHNvIG1hbnkgdGhpbmdzIGNoYW5naW5nIHlvdSd2ZSBhbHdheXMgbWFkZSBtZSBmZWVsIHNvIGxvdmVkIGFuZCwgaW4gaGluZHNpZ2h0LCBpdCdzIGZ1bm55IGhvdyBiZWZvcmUgYXQgdGhlIGVuZCBvZiBoaWdoIHNjaG9vbCBJIHdhcyBzbyBjb252aW5jZWQgb3VyIGZyaWVuZHNoaXAgd291bGQgZmFkZSBhd2F5LiBPZiBhbGwgdGhlIHRpbWUgd2UndmUgc3BlbnQgdG9nZXRoZXIsIHRoZXJlIGhhcyBzb21laG93IG5ldmVyIGJlZW4gYSBtb21lbnQgdGhhdCBJJ3ZlIGRvdWJ0ZWQgeW91ciBnb29kIGludGVudGlvbnMsIGFuZCBpdCdzIHdoeSBJIHdhcyBzbyBkcmF3biB0byB5b3UgaW4gdGhlIGZpcnN0IHBsYWNlLiBJJ20gc3VyZSBtb3N0IHBlb3BsZSBmZWVsIHRoZSBzYW1lIHdheSwgYW5kIHNvIGl0J3MgaW5ldml0YWJsZSB0aGF0IGdvb2QgdGhpbmdzIGhhdmUgYW5kIHdpbGwgY29tZSB5b3VyIHdheS4KCkkgYW0gY29udGludW91c2x5IGFtYXplZCBieSB5b3UuIFlvdSBhcmUgc28gc21hcnQgYW5kIGF3YXJlIGluIHNvIG1hbnkgd2F5cyB0aGF0IGl0IHNvbWV0aW1lcyBpdCBmZWVscyBsaWtlIHlvdSd2ZSBhbHJlYWR5IGxpdmVkIGxpZmUgYmVmb3JlLCBhbmQgd2VyZSBkcm9wcGVkIGJhY2sgb24gRWFydGggdG8gZ3VpZGUgdGhlIHdheSBmb3IgdGhlIGxvd2x5LCBsb3N0IGh1bWFucyBsaWtlIG1lIC0gYSBzdHlsaXNoIGd1YXJkaWFuIGFuZ2VsLiBCdXQgSSBhbHNvIGtpbmQgb2YgZ2V0IGl0LCBiZWNhdXNlIEkgY29uc3RhbnRseSBzZWUgeW91IHB1dHRpbmcgc28gbXVjaCB0aW1lIGFuZCB0aG91Z2h0IGludG8gaGVscGluZyBwZW9wbGUgYW5kIHNvbHZpbmcgcHJvYmxlbXMgdGhhdCB5b3Ugd2VyZSBib3VuZCB0byBkZXZlbG9wIHNvbWUgc29ydCBvZiB1bmNhbm55IGludGVsbGlnZW5jZSBmcm9tIHRoZSBzaGVlciBhbW91bnQgb2YgZWZmb3J0IHlvdSBwdXQgaW4uIFlvdSdyZSBsaWtlIG9uZSBvZiB0aG9zZSBjb21wdXRlciBzY2llbmNlIHN0dWRlbnRzIHRoYXQgbGl2ZSBhbmQgYnJlYXRoZSBjb21wdXRlcnMsIGV4Y2VwdCB3aXRoIGxpZmUgYW5kIHBlb3BsZS4KCkkgYW0gc28gYXBwcmVjaWF0aXZlIG9mIHRoZSBwdXNoIGFuZCBzdXBwb3J0IHRoYXQgeW91IGhhdmUgZ2l2ZW4gbWUgdGhyb3VnaG91dCBoaWdoIHNjaG9vbCBhbmQgdW5pLiBJIGtub3cgSSd2ZSBnaXZlbiB5b3Ugc2hpdCBpbiB0aGUgcGFzdCBhYm91dCBwdXNoaW5nIG1lIG91dCBvZiBteSBjb21mb3J0IHpvbmUsIGJ1dCBJIHRha2UgaXQgYmFjay4gRXZlcnl0aW1lIHlvdSBhY2NvbXBhbnkgbWUgdG8gYSBwdWJjcmF3bCwgdGFsayBtZSB1cCB0byBhdHRlbmRpbmcgYSByb2FkIHRyaXAsIG9yIGd1aWRlIG1lIHRocm91Z2ggYSAocHJvYmFibHkgdHJpdmlhbCkgaW50ZXJhY3Rpb24sIEkgZmVlbCBsaWtlIGEgbWFpZGVuIGluIGRpc3RyZXNzLCBhbmQgeW91J3JlIGEga25pZ2h0IHZhbGlhbnRseSBmaWdodGluZyBvZmYgdGhlIGVuY3JvYWNoaW5nIHRpbWVsaW5lIHdoZXJlIEknbSBhIGNydXN5IG11c3R5IHRlY2ggbW9ua2V5LiBXaGVuZXZlciBzb21lb25lIHByYWlzZXMgbXkgc29jaWFsIHNraWxscywgb3IgSSBwYXNzIGEgYmVoYXZpb3VyYWwgaW50ZXJ2aWV3LCBJIGRvbid0IHRoYW5rIGdvZCwgSSB0aGFuayBMYXZhbnlhIE1ldGhpbC4KCkkgY291bnQgbXkgbHVja3kgc3RhcnMgdGhhdCB0aGF0IE1ycyBTYW55YWwgbGVmdCB0aGUgc2Nob29sIGFuZCBNcnMgRnJhbmNvIGNvdWxkbid0IHBpY2sgdXAgdGhlIHNsYWNrLCBiZWNhdXNlIHRoYXQgcHV0IG1lIGluIGNsYXNzIHdpdGggeW91IGZvciB0aGF0IGV4dHJhIHllYXIuIEFuZCBJIGFtIHN0cmFuZ2VseSBncmF0ZWZ1bCBmb3IgbXkgd2VpcmQgdGhpbmcgd2l0aCBBbGZhIGJlY2F1c2Ugb3RoZXJ3aXNlLCBJIHdvdWxkbid0IGhhdmUgc3RhcnRlZCBiYXNrZXRiYWxsIHdpdGggeW91LCB3b3VsZG4ndCBoYXZlIHNhdCB3aXRoIHlvdSBpbiB0aGF0IHN0dWZmeSBHcmVlbnNxdWFyZSBLRkMgYW5kIGhhZCB0aG9zZSBkZWVwIHRhbGtzLCBhbmQgcmVhbGlzZWQgYWZ0ZXIgdGhhdCBmYXRlZnVsIGJhc2tldGJhbGwgZ2FtZSBqdXN0IGhvdyBncmVhdCBvZiBhIHBlcnNvbiB5b3UgYXJlLgoKQWxsIG9mIHRoZSBiYWQgdGhpbmdzIHRoYXQgaGFwcGVuZWQgaW4gcGFzdCBnZW51aW5lbHkgZmVlbCB0cml2aWFsIGNvbnNpZGVyaW5nIHRoZSBvdXRjb21lIGlzIHRoYXQgSSBzdGlsbCBoYXZlIHlvdSBhcyBteSBmcmllbmQuIE1heWJlIHlvdSdyZSBsaWtlIGNyYWNrIGlmIGl0IHdhcyBhIHBlcnNvbiwgYmVjYXVzZSBub2JvZHkgSSd2ZSBtZXQgY29tZXMgY2xvc2UgdG8gZmVlbGluZyBzbyB3b3J0aCBpdC4KCkkgYW0gc28gZ3JhdGVmdWwgdG8gaGF2ZSB5b3UsIGFuZCBJIGhvcGUgeW91ciAyMHMgYXJlIG9ubHkgdXAgZnJvbSBoZXJlLgoKVGhhbmsgeW91IGZvciBldmVyeXRoaW5nLCBLYWku"))
        
        sam = createSprite(dialogue, samAnimations)
        sam.play(listOf("posing", "jumping", "pointingUp", "pointingLeft", "browRaise", "headShake", "angry", "accusatory"))

        fitDialogueText()
    }

    private fun addDialogue(message: String) {
        val dialogue = document.getElementById("dialogue") ?: return
        
        val text = document.createElement("p")
        text.textContent = message
        
        dialogue.appendChild(text)
    }

    private fun fitDialogueText() {
        val dialogue = document.getElementById("dialogue") as? HTMLElement ?: return

        var fontSize = 24
        dialogue.style.fontSize = "${fontSize}px"

        while (dialogue.scrollHeight - 15 > dialogue.clientHeight && fontSize > 8) {
            fontSize--
            dialogue.style.fontSize = "${fontSize}px"
        }
    }
}

class MatchingCardsGame(
    private val parent: HTMLElement,
    private val imagePaths: List<String>,
    private val onComplete: () -> Unit
) {
    private data class Card(
        val id: Int,
        val imagePath: String,
        var element: HTMLElement? = null,
        var matched: Boolean = false
    )

    private val cards = (imagePaths + imagePaths)
        .shuffled()
        .mapIndexed { index, imagePath ->
            Card(index, imagePath)
        }

    private var firstCard: Card? = null
    private var secondCard: Card? = null
    private var locked = false
    private var matches = 0

    fun start() {
        parent.innerHTML = ""

        val grid = document.createElement("div") as HTMLElement
        grid.id = "matching-grid"
        parent.appendChild(grid)

        cards.forEach { card ->
            val element = document.createElement("button") as HTMLElement
            element.className = "matching-card"

            val image = document.createElement("img") as HTMLImageElement
            image.src = card.imagePath
            image.className = "matching-image"

            element.appendChild(image)

            // Hide the image initially.
            image.style.visibility = "hidden"

            element.addEventListener("click") {
                flip(card)
            }

            card.element = element
            grid.appendChild(element)
        }
    }

    private fun flip(card: Card) {
        if (locked || card.matched || card == firstCard) return

        val element = card.element ?: return
        val image = element.querySelector("img") as? HTMLImageElement ?: return

        image.style.visibility = "visible"
        element.classList.add("flipped")

        if (firstCard == null) {
            firstCard = card
            return
        }

        secondCard = card
        locked = true

        window.setTimeout({
            checkMatch()
        }, 700)
    }

    private fun checkMatch() {
        val first = firstCard ?: return
        val second = secondCard ?: return

        if (first.imagePath == second.imagePath) {
            first.matched = true
            second.matched = true

            first.element?.classList?.add("matched")
            second.element?.classList?.add("matched")

            matches++

            if (matches == imagePaths.size) {
                window.setTimeout({
                    onComplete()
                }, 500)
            }
        } else {
            hideCard(first)
            hideCard(second)
        }

        firstCard = null
        secondCard = null
        locked = false
    }

    private fun hideCard(card: Card) {
        val element = card.element ?: return
        val image = element.querySelector("img") as? HTMLImageElement ?: return

        image.style.visibility = "hidden"
        element.classList.remove("flipped")
    }

    fun revealAll() {
        cards.forEach { card ->
            val element = card.element ?: return@forEach
            val image = element.querySelector("img") as? HTMLImageElement ?: return@forEach

            image.style.visibility = "visible"
            element.classList.add("flipped")
        }

        locked = true
    }
}

/*************************************************************************/
// Driver

fun main() {
    Game().start()
}

/*************************************************************************/
// Helpers

fun decode(encoded: String): String {
    return js("decodeURIComponent(escape(atob(encoded)))")
}

fun encode(encoded: String): String {
    return window.btoa(encoded)
}
