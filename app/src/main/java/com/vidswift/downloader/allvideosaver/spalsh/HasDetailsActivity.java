package com.vidswift.downloader.allvideosaver.spalsh;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vidswift.downloader.allvideosaver.databinding.ActivityHasDetailsBinding;

public class HasDetailsActivity extends BaseActivity {
    private ActivityHasDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHasDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarLayout.btnBack.setOnClickListener(v -> onBackPressed());
        String category = getIntent().getStringExtra("category");


        binding.toolbarLayout.headerTitle.setText(category);

        binding.txtHashtags.setText(getHashtagsForCategory(category));


        binding.txtHashtags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hashtags = binding.txtHashtags.getText().toString();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hashtags", hashtags);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(HasDetailsActivity.this, "Hashtags copied!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String getHashtagsForCategory(String category) {
        switch (category) {
            case "Happy":
                return "#happy #smile #joy #positivevibes #happiness #fun #goodvibes #love #laugh #lifeisgood #sunshine #cheerful #grateful #blessed #positivity #mood #feelgood #beautifulday #enjoylife #goodday #goodenergy #peace #serotonin #smilesformiles #selflove #wellness #kindness #uplift #inspiration #vibes #goals #freshstart #hopeful #peaceofmind #mentalhealth #healing #goodtimes #goodmorning #thankful #todayisagoodday #staypositive #bepositive #positivemind #happyplace #content #positiveenergy #positivequotes #positivelife #optimism #vibesonly";

            case "Love":
                return "#love #romance #couplegoals #relationshipgoals #affection #kisses #hug #sweetheart #truelove #date #partner #lover #feelings #loveher #lovehim #togetherforever #youandme #relationship #cutecouple #crush #flirting #cuddles #engaged #weddingvibes #fallinlove #hearts #adorable #soulmate #romantic #passion #lovestory #myworld #lovelife #foreverlove #iloveyou #boyfriend #girlfriend #bestcouple #kissme #romanticquotes #mylove #hugsandkisses #bae #bemyvalentine #partnersincrime #relationshipstatus #inlove #couplesofinstagram #lovebirds #purelove";

            case "Sad":
                return "#sad #lonely #broken #depressed #heartbreak #tears #hurt #pain #lost #alone #crying #unloved #mentalhealth #melancholy #blue #down #depression #emotions #suffering #darkness #grief #empty #struggle #silenttears #numb #overthinking #mood #low #darkthoughts #isolation #sadnessquotes #hurtquotes #crybaby #rainydays #emotional #wounded #fading #hopeless #sadtruth #brokeninside #lifeishard #rawemotions #soulache #missingyou #nostalgia #deepfeelings #staystrong #troubledmind #cloudymind #regret #loneliness";

            case "Friendship":
                return "#friendship #friends #bestfriends #bffs #squadgoals #loyalty #friendshipgoals #goodtimes #friendshipquotes #funwithfriends #bestie #friendlove #friendvibes #foreverfriends #friendbond #mygang #brotherhood #sisterhood #closefriends #laughs #funnyfriends #bestiesforlife #trust #realfriends #bffgoals #friendshipmatters #rideordie #togetherforever #chillin #truefriend #friendstime #hangout #bestfriendgoals #crazyfriends #lovemyfriends #lifewithfriends #team #supportsystem #friendcircle #partyfriends #trustedones #foreverbond #laughinghard #insidejokes #friendzone #friendgang #madfriends #frienddate #funbuddies #socialcircle";

            case "Angry":
                return "#angry #rage #fury #mad #frustration #pissedoff #heated #dontmesswithme #angerissues #explosive #irritated #annoyed #agitated #madworld #boiling #emotions #hotheaded #outburst #temper #stayaway #grr #shouting #yelling #drama #venting #rant #arguing #mood #badvibes #triggered #tension #chaos #fightmode #frustrated #roadrage #irritable #toxic #ragequit #overit #negativeenergy #emotionalstorm #stressthrowback #angermode #loudthoughts #donttalktome #heatedmind #imdone #walkaway #nonsense #boilover #burst";

            case "Motivation":
                return "#motivation #inspiration #goals #success #ambition #grind #workhard #dreambig #positivity #growth #mindset #hustle #focus #goalsetter #achieve #selfgrowth #powerful #confidence #nevergiveup #keepgoing #believeinyourself #motivated #dedication #progress #discipline #selfimprovement #dreamchaser #winning #mindovermatter #productivity #stayfocused #workmode #entrepreneurmindset #strongmind #levelup #pushyourself #getafterit #determination #actiontaker #riseandgrind #innerstrength #inspiredaily #successmindset #workethic #positiveenergy #lifegoals #keepmoving #thinkbig #hustlemode #personaldevelopment";

            case "Self-Love":
                return "#selflove #selfcare #mentalhealth #confidence #loveyourself #beyou #innerpeace #selfworth #selfrespect #healing #growth #positivemindset #selfacceptance #mindfulness #selfesteem #youareenough #selfhelp #selfgrowth #wellbeing #mentalhealthawareness #selfcompassion #healthyself #selftrust #innerwork #boundaries #selfreflection #glowup #peacewithin #authenticself #selfkindness #nurtureyourself #innerstrength #selfsupport #emotionalwellness #bepatient #selfhealing #selfawareness #innerlight #selfempowerment #lovefromwithin #selfvalue #personaldevelopment #mindbodysoul #takecareofyou #growthmindset #selfconfidence #selfdiscovery #grateful #loveyourvibes #selfcarematters";

            case "Aesthetic":
                return "#aesthetic #vibes #moodygrams #artsy #dreamy #aestheticfeed #visuals #softvibes #minimalism #vintageaesthetic #indieaesthetic #pastelvibes #retrovibes #photooftheday #aestheticstyle #mochaaesthetic #brownvibes #monochrome #vibesoflife #calmvibes #aestheticphotography #prettypictures #vintagevibes #artfeed #cleanfeed #pastelaesthetic #pasteltones #moodboard #aestheticlook #inspirationalvibes #filtered #cozyvibes #moodytones #flatlaystyle #artinspo #moodvibes #aestheticpage #lightaesthetic #retrostyle #softcolor #feedaesthetic #photoset #neutraltones #instagramstyle #aestheticfeel #cutevibes #calmaesthetic #minimalvibe #aestheticmood #curatedvibes";

            case "Alone":
                return "#alone #solitude #loner #introvert #quiet #peaceofmind #selftime #reflection #deepthoughts #loneliness #selfdiscovery #innerpeace #mymind #thinking #melancholy #nightsalone #bemyself #independent #justme #onmyown #solitudestate #quietmoments #foreveralone #peaceandquiet #personalspace #deepfeelings #aloneness #soulsearching #selfjourney #alonewiththoughts #notlonely #serenity #quiettime #lonewolf #nightthoughts #deepmind #calmspace #alonebutnotlonely #silentworld #chillvibes #mentalclarity #quietperson #peacewithin #stillness #restmode #lowkeylife #quietmind #peacefulstate #alonespace";

            case "Attitude":
                return "#attitude #confidence #bold #badass #swagger #power #bossmode #grind #savage #staystrong #winner #selfbelief #fearless #ownit #youcandoit #unstoppable #mood #idgaf #alpha #baddie #confident #noexcuses #goalgetter #mindset #ambition #focus #positivevibes #determined #grit #hustlehard #limitless #nevergiveup #motivation #leader #selfrespect #goaldigger #fearlessmind #boldmoves #mindovermatter #icandothis #proud #attitudequotes #fierce #doitforyou #courageous #innerpower #riseup #alphamindset #mindsetmatters #doyourbest";

            default:
                return "#hashtag #trending #popular #instagood #explorepage #viral #besthashtags #tagme #photooftheday #likesforlikes #foryou #reels";
        }
    }

    public void onBackPressed() {
        myBackActivity();
    }
}