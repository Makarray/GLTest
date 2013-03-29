package shrider.uhe.graphics;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
	private GLViewer view;
	private Button button_up;
	private Button button_down;
	private Button button_left;
	private Button button_right;
	private Button button_rotate;

	private GLRenderer render;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new GLViewer(this);

		/* remove the title bar at the top of screen */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* remove the status bar, make it full screen */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		//set layout
		setContentView(R.layout.activity_main);

		button_down=(Button)findViewById(R.id.down);
		button_up=(Button)findViewById(R.id.up);
		button_right=(Button)findViewById(R.id.right);
		button_left=(Button)findViewById(R.id.left);
		button_rotate=(Button)findViewById(R.id.rotate);
		Button button_forward=(Button)findViewById(R.id.forward);
		Button button_back=(Button)findViewById(R.id.back);
		Button button_roll=(Button)findViewById(R.id.roll);
		Button button_tilt=(Button)findViewById(R.id.tilt);
		RadioButton radioWorld = (RadioButton)findViewById(R.id.radio0);
		RadioButton radioSquare = (RadioButton)findViewById(R.id.radio1);
		RadioButton radioPyramid = (RadioButton)findViewById(R.id.radio2);
		RadioButton radioLight = (RadioButton)findViewById(R.id.lightBut);
		
		radioWorld.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(7);
			}
		});
		radioSquare.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(8);
			}
		});
		radioPyramid.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(9);
			}
		});
		radioLight.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(12);
			}
		});
		button_forward.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(0);
			}
		});
		button_back.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(1);
			}
		});
		button_down.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(10);
			}
		});
		button_up.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(11);
			}
		});
		button_left.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(2);
			}
		});
		button_right.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(3);
			}
		});
		button_rotate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(5);

			}
		});
		button_roll.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(6);
			}
		});
		button_tilt.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			render.Click(4);
			}
		});
		
		View v = findViewById(R.id.textView1);
        ViewGroup parent = (ViewGroup) v.getParent();

        view.setLayoutParams(parent.getLayoutParams());
        
        /* replace by removing and adding */
        int temp= parent.indexOfChild(v);
        parent.removeViewAt(temp);
        parent.addView (view, temp);
	}

	protected void onResume() {
		super.onResume();
		view.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	//	view.onResume();
	}
	
	protected void onPause() {
		super.onPause();
		view.onPause();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public class GLViewer extends GLSurfaceView{
		
        public GLViewer(Context c){
        	super(c);
            render = new GLRenderer(c);
            setRenderer(render);
            setRenderMode(RENDERMODE_CONTINUOUSLY);
        }
        		
	}
}
