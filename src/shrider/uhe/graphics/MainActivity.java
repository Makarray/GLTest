package shrider.uhe.graphics;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button button_up;
	private Button button_down;
	private Button button_left;
	private Button button_right;
	private Button button_rotate;

	private Viewer view;
	private GLRenderer render;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* remove the title bar at the top of screen */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* remove the status bar, make it full screen */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		view = new Viewer(this);
		view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		//set layout
		setContentView(R.layout.activity_main);

		button_down=(Button)findViewById(R.id.down);
		button_up=(Button)findViewById(R.id.up);
		button_right=(Button)findViewById(R.id.right);
		button_left=(Button)findViewById(R.id.left);
		button_rotate=(Button)findViewById(R.id.rotate);

		button_down.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			//	render.Down();
			}
		});
		button_up.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			//	render.Up();
			}
		});
		button_left.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			//	render.Left();
			}
		});
		button_right.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			//	render.Right();
			}
		});
		button_rotate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			//	render.Rotate();

			}
		});
		
		View v = findViewById(R.id.GLView);
        ViewGroup parent = (ViewGroup) v.getParent();

        view.setLayoutParams(parent.getLayoutParams());
        
        /* replace by removing and adding */
        int temp= parent.indexOfChild(v);
        parent.removeViewAt(temp);
        parent.addView (view, temp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public class Viewer extends GLSurfaceView{
        public Viewer(Context c){
        	super(c);
            render = new GLRenderer(c);
            setRenderer(render);
            setRenderMode(RENDERMODE_CONTINUOUSLY);
        }
        		
	}
}